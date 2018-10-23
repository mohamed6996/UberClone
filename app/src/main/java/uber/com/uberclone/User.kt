package uber.com.uberclone

import android.support.annotation.Keep

@Keep
data class User(var email:String = "",var password:String = "",var name:String = "",var number:String = "")