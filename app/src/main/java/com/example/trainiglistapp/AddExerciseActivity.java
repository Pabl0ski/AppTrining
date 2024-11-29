package com.example.trainiglistapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//Esta es la pagina que añade los ejercicios cuando le das al boton

public class AddExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        // Referencias a los elementos de la interfaz
        EditText etExerciseName = findViewById(R.id.etExerciseName);
        Spinner spinnerExerciseType = findViewById(R.id.spinnerExerciseType);
        Spinner spinnerExerciseLevel = findViewById(R.id.spinnerExerciseLevel);
        Button btnSaveExercise = findViewById(R.id.btnSaveExercise);

        // Configurar el botón para guardar el ejercicio
        btnSaveExercise.setOnClickListener(view -> {
            String exerciseName = etExerciseName.getText().toString().trim();
            String exerciseType = spinnerExerciseType.getSelectedItem().toString();
            String exerciseLevel = spinnerExerciseLevel.getSelectedItem().toString();

            if (!exerciseName.isEmpty()) {
                // Devolver el ejercicio con sus datos a la actividad principal
                Intent resultIntent = new Intent();
                resultIntent.putExtra("EXERCISE_NAME", exerciseName);
                resultIntent.putExtra("EXERCISE_TYPE", exerciseType);
                resultIntent.putExtra("EXERCISE_LEVEL", exerciseLevel);
                setResult(RESULT_OK, resultIntent);
                finish(); // Cierra la actividad
            } else {
                Toast.makeText(this, "Por favor, introduce un nombre", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


