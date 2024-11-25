package com.example.trainiglistapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        // Referencias al campo de texto y botón
        EditText etExerciseName = findViewById(R.id.etExerciseName);
        Button btnSaveExercise = findViewById(R.id.btnSaveExercise);

        // Configurar el botón para guardar el ejercicio
        btnSaveExercise.setOnClickListener(view -> {
            String exerciseName = etExerciseName.getText().toString().trim();

            if (!exerciseName.isEmpty()) {
                // Devolver el ejercicio a la actividad principal
                Intent resultIntent = new Intent();
                resultIntent.putExtra("EXERCISE_NAME", exerciseName);
                setResult(RESULT_OK, resultIntent);
                finish(); // Cierra la actividad
            } else {
                Toast.makeText(this, "Por favor, introduce un nombre", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

