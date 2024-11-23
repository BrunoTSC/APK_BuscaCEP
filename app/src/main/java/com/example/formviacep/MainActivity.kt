package com.example.formviacep

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.formviacep.api.Api
import com.example.formviacep.databinding.ActivityMainBinding
import com.example.formviacep.model.Endereco
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // O view binding pega todos os objetos(componentes) da activity xml, agora estão sendo vistos em nosso kotlin.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        window.statusBarColor = Color.parseColor("#0723B6")
        val actionBar =
            supportActionBar //actiobar e tubar sao as duas partes superiores do apk, actionbar é a parte de baixo e a tubar a de cima onde fica data hora e bateria, onde configuramos essa cor.
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#0723B6")))

        //configurando o retrofit

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()) //conversão
            .baseUrl("https://viacep.com.br/ws/")
            .build()//para construir tudo isso que ele está fazendo.
            .create(Api::class.java) //criação do que a gente fez, configurando a API.

        binding.btBuscarCep.setOnClickListener { // configurar a parte do botão.
            val cep = binding.editCep.text.toString()
            if (cep.isEmpty()) { //isEmpty - se o campo cep estiver vazio:
                Toast.makeText(this, "Preencher o campo CEP", Toast.LENGTH_SHORT).show() //Toast é um alerta, aparece para dar o recado e depois some. Possui um tempo configurado.

            } else {
                retrofit.setEndereco(cep).enqueue(object : Callback<Endereco> { // vai trazer o caminho que está no arquivo API.//object herda o callback e traz os dados que estao em endereco.
                    override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
                        if (response.code() == 200) { //if pega o response acima e verifica se ele retorna com um code(código), neste caso o 200 que é o OK da API.
                            val logradouro = response.body()?.logradouro.toString()
                            val bairro = response.body()?.bairro.toString()
                            val localidade = response.body()?.localidade.toString()
                            val uf = response.body()?.uf.toString()
                            setFormularios(logradouro, bairro, localidade, uf)
                        } else {
                            Toast.makeText(applicationContext, "Cep Errado!!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<Endereco>, t: Throwable) {
                        Toast.makeText(applicationContext, "Erro inesperado!", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        }
    }
       private fun setFormularios(logradouro: String, bairro: String, localidade: String, uf: String){ //passa os parametros
            binding.editLogradouro.setText(logradouro)
            binding.editBairro.setText(bairro)
            binding.editCidade.setText(localidade)
            binding.editEstado.setText(uf)
        }
    }













