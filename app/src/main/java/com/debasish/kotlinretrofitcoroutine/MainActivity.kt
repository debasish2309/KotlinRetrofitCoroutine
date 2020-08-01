package com.debasish.kotlinretrofitcoroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var retService: AlbumService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retService = RetrofitInstance
                                 .getRetrofitInstance()
                                 .create(AlbumService::class.java)


       // getRequestWithQueryParameters()
       // getRequestWithPathParameters()
        uploadAlbum()

    }

    private fun getRequestWithQueryParameters() {

        val responseLiveData: LiveData<Response<Albums>> = liveData {
            //    val response: Response<Albums> = retService.getAlbums()
            val response: Response<Albums> = retService.getSortedAlbums(3)
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val albumList : MutableListIterator<AlbumsItem>? = it.body()?.listIterator()
            if(albumList != null){
                while (albumList.hasNext()) {
                    val albumsItem : AlbumsItem = albumList.next()
                    Log.i("MYTAG",albumsItem.title)
                    val result = " " + "Album Title : ${albumsItem.title}" + "\n" +
                            " " + "Album Id : ${albumsItem.id}" + "\n"  +
                            " " + "User Id : ${albumsItem.userId}" + "\n\n\n"
                    textView.append(result)
                }
            }
        })
    }

    private fun getRequestWithPathParameters(){

        val pathResponse: LiveData<Response<AlbumsItem>> = liveData {
            val response:Response<AlbumsItem> = retService.getAlbum(3)
            emit(response)
        }

        pathResponse.observe(this, Observer {
            val title:String? = it.body()?.title
            Toast.makeText(applicationContext,title,Toast.LENGTH_SHORT).show();
        })
    }

    private fun uploadAlbum(){
        val album = AlbumsItem(0,"My Title",3)

        val postResponse: LiveData<Response<AlbumsItem>> = liveData {
            val response:Response<AlbumsItem> = retService.uploadAlbum(album)
            emit(response)
        }

        postResponse.observe(this, Observer { it
            val receivedAlbumsItem: AlbumsItem? = it.body()
            val result: String = " " + "Album Title : ${receivedAlbumsItem?.title}" + " "
                          " " + " Album id: ${receivedAlbumsItem?.id}" + "\n"
                          " " + " User Id : ${receivedAlbumsItem?.userId} " + "\n\n\n"
            textView.text = result
        })
    }
}