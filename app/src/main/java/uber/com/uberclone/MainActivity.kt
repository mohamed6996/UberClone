package uber.com.uberclone

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var db: FirebaseDatabase
    private lateinit var usersRef: DatabaseReference

    private lateinit var signInBtn: Button
    private lateinit var registerBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        usersRef = db.getReference("users")

        signInBtn = findViewById(R.id.sign_in_btn)
        registerBtn = findViewById(R.id.register_btn)

        registerBtn.setOnClickListener {
            showRegisterDialog()
        }

        signInBtn.setOnClickListener {
            showSignInDialog()
        }
    }


    private fun showRegisterDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Register")
        dialog.setMessage("Please use a valid email to register")

        val inflater = LayoutInflater.from(this)
        val registerLayout = inflater.inflate(R.layout.register, null)

        dialog.setView(registerLayout)

        val emailTxtInput = registerLayout.findViewById<TextInputLayout>(R.id.txt_input_email)
        val passwordTxtInput = registerLayout.findViewById<TextInputLayout>(R.id.txt_input_password)
        val nameTxtInput = registerLayout.findViewById<TextInputLayout>(R.id.txt_input_name)
        val numberTxtInput = registerLayout.findViewById<TextInputLayout>(R.id.txt_input_number)

        dialog.setPositiveButton("Register") { dialogInterface, i ->

            val email = emailTxtInput.editText?.text.toString()
            val password = passwordTxtInput.editText?.text.toString()
            val name = nameTxtInput.editText?.text.toString()
            val number = numberTxtInput.editText?.text.toString()

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = User()
                    user.email = email
                    user.password = password
                    user.name = name
                    user.number = number

                    usersRef.child(mAuth.currentUser?.uid!!).setValue(user).addOnCompleteListener {
                        dialogInterface.dismiss()
                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                    }
                        .addOnFailureListener {
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        }


                    // dialogInterface.dismiss()
                } else {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }


        }

        dialog.setNegativeButton("Cancel") { dialogInterface, i ->
            dialogInterface.dismiss()
        }

        dialog.show()


    }


    private fun showSignInDialog() {

        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Sign in")

        val inflater = LayoutInflater.from(this)
        val signInLayout = inflater.inflate(R.layout.sign_in, null)

        dialog.setView(signInLayout)

        val emailTxtInput = signInLayout.findViewById<TextInputLayout>(R.id.txt_input_email_login)
        val passwordTxtInput = signInLayout.findViewById<TextInputLayout>(R.id.txt_input_password_login)

        dialog.setPositiveButton("Sign In") { dialogInterface, i ->

            val email = emailTxtInput.editText?.text.toString()
            val password = passwordTxtInput.editText?.text.toString()

            mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    dialogInterface.dismiss()
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        }

        dialog.setNegativeButton("Cancel") { dialogInterface, i ->
            dialogInterface.dismiss()
        }

        dialog.show()


    }


}
