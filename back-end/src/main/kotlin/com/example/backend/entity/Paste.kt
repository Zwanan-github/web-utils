package com.example.backend.entity

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.util.Date

class Paste : Serializable {
    var name: String = "";

    var content: String = "";

    var password: String = "";

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var createTime: Date = Date();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var updateTime: Date = Date();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var expireTime: Date = Date();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    var lastBrowse: Date = Date();

    var expire: Long = 0;

    var browseTimes: Long = 0;
}