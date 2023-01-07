package cn.rtast.rmusic.music

import cn.rtast.rmusic.data.netease.ProfileModel
import cn.rtast.rmusic.data.netease.login.LoginResponseModel
import cn.rtast.rmusic.data.netease.search.SearchResponseModel
import cn.rtast.rmusic.data.netease.search.Song
import cn.rtast.rmusic.data.netease.url.Data
import cn.rtast.rmusic.data.netease.url.UrlModel
import com.google.gson.Gson
import java.io.File
import java.net.URL

class Music163 {
    private val gson = Gson()
    private val rootApi = "https://api.163.rtast.cn"
    private val cookie: String? = null

    fun login(email: String, password: String) {
        val result = URL("$rootApi/login?email=$email&password=$password").readText()
        val json = gson.fromJson(result, LoginResponseModel::class.java)
        val file = File("./rmusic/profile.json")
        if (!file.exists()) {
            file.createNewFile()
        } else {
            file.delete()
            file.createNewFile()
        }
        val profile = ProfileModel(
            json.profile.avatarUrl,
            json.account.id,
            json.profile.backgroundUrl,
            json.cookie,
            json.profile.nickname
        )
        file.writeText(gson.toJson(profile))
    }

    fun logout() {
        val file = File("./rmusic/cookie.json")
        if (file.exists()) {
            file.delete()
        }
    }

    fun cloudsearch(keyword: String): List<Song> {
        // 将空格替换为编码后的空格
        val result = URL("$rootApi/cloudsearch?keywords=${keyword.replace(" ", "%20")}").readText()
        return gson.fromJson(result, SearchResponseModel::class.java).result.songs.subList(0, 10)
    }

    fun getSongUrl(id: Int): Data {
        val result = URL("$rootApi/song/url?id=$id&br=320000&cookie=$cookie").readText()
        return gson.fromJson(result, UrlModel::class.java).data[0]
    }

    fun lyric(id: Int) {
        val result = URL("$rootApi/lyric/id=$id&cookie=$cookie").readText()
    }
}