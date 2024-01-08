package com.example.backend.service

import com.example.backend.entity.Paste
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class PasteService{

    @Qualifier("getRedisTemplate")
    @Autowired
    lateinit var template: RedisTemplate<String, Paste>;

    @Transactional
    fun add(paste: Paste){
        // 使用template.execute() 开启事务
        template.execute {
            // 开始事务
            it.multi();
            paste.createTime = Date();
            paste.updateTime = Date();
            paste.lastBrowse = Date();
            paste.expireTime = Date(paste.createTime.time + paste.expire * 3600 * 1000);
            template.opsForValue().set(paste.name, paste, paste.expire * 3600, TimeUnit.SECONDS)
            // 执行事务
            it.exec();
            return@execute true;
        }
    }

    fun get(name: String): Paste {
        val paste: Paste? = template.opsForValue().get(name);
        return paste ?: Paste();
    }

    @Transactional
    fun browse(paste: Paste): Paste {
        val cur: Paste? = template.opsForValue().get(paste.name);
        if (cur?.password != paste.password) {
            return Paste();
        }
        cur.let {
            // 使用template.execute() 开启事务
            template.execute {
                // 开始事务
                it.multi();
                // 设置paste对象的新值
                // 浏览次数 + 1
                cur.browseTimes += 1;
                // 更新最后浏览时间
                cur.lastBrowse = Date();
                // 如果原来的 key 有过期时间，那么就重新设置redis的值，设置过期时间
                val expire: Long = template.getExpire(cur.name);
                if (expire > 0) {
                    template.opsForValue().set(cur.name, cur, expire, TimeUnit.SECONDS);
                }
                // 执行事务
                it.exec();
                return@execute true;
            }
            return cur;
        }
    }

    @Transactional
    fun update(paste: Paste) {
        // 使用template.execute() 开启事务
        template.execute {
            // 开始事务
            it.multi();
            val expire: Long = template.getExpire(paste.name);
            paste.updateTime = Date();
            paste.expireTime = Date(paste.createTime.time + paste.expire * 3600 * 1000);
            // 修改后的时间小于当前时间, 则过期删掉
            if(paste.expireTime.time < Date().time){
                template.delete(paste.name);
                return@execute true;
            }
            template.opsForValue().set(paste.name, paste, paste.expire * 3600 - expire, TimeUnit.SECONDS);
            // 执行事务
            it.exec();
            return@execute true;
        }
    }

    fun delete(name: String) = template.delete(name);
}

