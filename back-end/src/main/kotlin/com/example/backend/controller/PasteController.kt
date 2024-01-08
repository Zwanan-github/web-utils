package com.example.backend.controller

import com.example.backend.entity.Paste
import com.example.backend.service.PasteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/paste")
class PasteController{


    @Autowired
    lateinit var pasteService: PasteService;

    @PostMapping
    fun add(@RequestBody paste : Paste, @RequestParam oldPassword: String): Paste{
        // 先判断是否存在, 存在就修改, 不存在就添加
        val old: Paste = pasteService.get(paste.name)
        if (old.password != oldPassword) {
            return Paste();
        }
        if("" == old.name){
            pasteService.add(paste);
        } else {
            pasteService.update(paste);
        }
        return pasteService.browse(paste);
    }

    /**
     * 获取该 paste 是否 公开 (有密码就返回 true)
     */
    @GetMapping("/private/{name}")
    fun isPrivate(@PathVariable("name") name: String): Boolean {
        val paste: Paste = pasteService.get(name)
        return paste.password == ""
    }

    /**
     * 获取详细信息
     */
    @PostMapping("/detail")
    fun browse(@RequestBody paste: Paste) = pasteService.browse(paste)

    @DeleteMapping("/{name}")
    fun delete(@PathVariable("name") name: String) = pasteService.delete(name)

}