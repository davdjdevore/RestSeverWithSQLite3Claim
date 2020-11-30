// 893845891
// David Devore
// Problem 3

package org.csuf.cspc411

import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.utils.io.*
import org.csuf.cspc411.Dao.Database
import com.almworks.sqlite4java.SQLiteConnection
import org.csuf.cspc411.Dao.Claim.Claim
import org.csuf.cspc411.Dao.Claim.ClaimDao

fun main(args: Array<String>): Unit {
    // Register PersonStore callback functions

    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    routing {

        post("/ClaimService/add") {
            val contType = call.request.contentType()
            val data = call.request.receiveChannel()
            val dataLength = data.availableForRead
            var output = ByteArray(dataLength)
            data.readAvailable(output)
            val str = String(output)

            // Json deserialization
            var cObj1 : Claim
            cObj1 = Gson().fromJson(str, Claim::class.java)
            val dao = ClaimDao().addClaim(cObj1)
            val dbObj = Database.getInstance()

            println("${cObj1.toString()}")
            println("HTTP message is using POST method with /post ${contType} ${str}")
            call.respondText("The POST request was successfully processed.",
                    status= HttpStatusCode.OK, contentType = ContentType.Text.Plain)
        }


        get("/ClaimService/getAll") {
            var cList = ClaimDao().getAllClaims()

            val respJsonStr = Gson().toJson(cList)

            call.respondText(respJsonStr, status= HttpStatusCode.OK, contentType= ContentType.Application.Json)
        }
    }
}