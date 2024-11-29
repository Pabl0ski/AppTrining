package com.example.trainiglistapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

//Este es el codigo de la pagina donde se ven los ejercicios añadidos

public class ExerciseListActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ADD_EXERCISE = 1; // Código para el intent de añadir ejercicio
    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private List<Exercise> exerciseList, filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar listas de ejercicios
        exerciseList = new ArrayList<>();
        loadInitialExercises();
        filteredList = new ArrayList<>(exerciseList);

        // Configurar adaptador
        adapter = new ExerciseAdapter(filteredList);
        recyclerView.setAdapter(adapter);

        // Cargar ejercicios desde SharedPreferences
        loadExercisesFromPreferences();

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        filteredList = new ArrayList<>(exerciseList);

        adapter = new ExerciseAdapter(filteredList);
        recyclerView.setAdapter(adapter);


        // Configurar Spinner para filtrar por nivel
        Spinner spinnerFilter = findViewById(R.id.spinnerFilter);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.level_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(spinnerAdapter);


        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLevel = parent.getItemAtPosition(position).toString();
                filterExercises(selectedLevel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filteredList.clear();
                filteredList.addAll(exerciseList);
                adapter.notifyDataSetChanged();
            }
        });

        // Configurar FloatingActionButton para añadir ejercicios
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ExerciseListActivity.this, AddExerciseActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_EXERCISE); // Lanzar AddExerciseActivity esperando un resultado
        });

        FloatingActionButton fabDelete = findViewById(R.id.fabDelete);
        fabDelete.setOnClickListener(view -> {
            if (!filteredList.isEmpty()) {
                // Eliminar el último ejercicio de la lista filtrada
                int lastIndex = filteredList.size() - 1;
                Exercise removedExercise = filteredList.remove(lastIndex);
                adapter.notifyItemRemoved(lastIndex);

                // También eliminar de la lista original si es necesario
                exerciseList.remove(removedExercise);

                // Mostrar Snackbar para deshacer
                Snackbar.make(view, removedExercise.getName() + " eliminado", Snackbar.LENGTH_LONG)
                        .setAction("Deshacer", undoView -> {
                            // Restaurar el ejercicio eliminado
                            filteredList.add(lastIndex, removedExercise);
                            exerciseList.add(removedExercise);
                            adapter.notifyItemInserted(lastIndex);
                        })
                        .show();
            } else {
                // Si no hay ejercicios, mostrar un mensaje
                Snackbar.make(view, "No hay ejercicios para eliminar", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Guardar ejercicios en SharedPreferences
        saveExercisesToPreferences();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterExercises(String level) {
        if (level.equals("Todos")) {
            filteredList.clear();
            filteredList.addAll(exerciseList);
        } else {
            filteredList.clear();
            for (Exercise exercise : exerciseList) {
                if (exercise.getLevel().equals(level)) {
                    filteredList.add(exercise);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void loadInitialExercises() {
        // Cargar algunos ejercicios iniciales
        exerciseList.add(new Exercise("Sentadillas", "Fuerza", "Intermedio"));
        exerciseList.add(new Exercise("Flexiones", "Fuerza", "Principiante"));
        exerciseList.add(new Exercise("Plancha", "Core", "Avanzado"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_EXERCISE && resultCode == RESULT_OK) {
            if (data != null) {
                // Obtener los datos del ejercicio creado
                String name = data.getStringExtra("EXERCISE_NAME");
                String type = data.getStringExtra("EXERCISE_TYPE");
                String level = data.getStringExtra("EXERCISE_LEVEL");

                // Añadir el nuevo ejercicio a la lista principal
                Exercise newExercise = new Exercise(name, type, level);
                exerciseList.add(newExercise);

                // Actualizar la lista filtrada
                filteredList.add(newExercise);
                adapter.notifyDataSetChanged(); // Actualizar la vista
            }
        }
    }

    // Métodos para añadir y eliminar ejercicios (opcional, no usados directamente)
    private void addExercise() {
        exerciseList.add(new Exercise("Nuevo Ejercicio", "Fuerza", "Principiante"));
        adapter.notifyItemInserted(filteredList.size() - 1);
    }

    private void removeLastExercise() {
        if (!filteredList.isEmpty()) {
            int lastIndex = filteredList.size() - 1;
            filteredList.remove(lastIndex);
            adapter.notifyItemRemoved(lastIndex);
        }
    }

    /* Método para guardar la lista de ejercicios*/
    private void saveExercisesToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("exercise_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(exerciseList); // Convertir la lista de ejercicios a JSON
        editor.putString("exercise_list", json);
        editor.apply(); // Guardar cambios
    }

    // Método para cargar la lista de ejercicios desde SharedPreferences
    private void loadExercisesFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("exercise_prefs", MODE_PRIVATE);
        String json = sharedPreferences.getString("exercise_list", null);

        if (json != null) {
            Gson gson = new Gson();
            // Aquí usamos TypeToken correctamente para obtener el tipo adecuado
            TypeToken<List<Exercise>> token = new TypeToken<List<Exercise>>() {};
            exerciseList = gson.fromJson(json, token.getType());
        } else {
            exerciseList = new ArrayList<>(); // Si no hay datos, inicializar la lista vacía
        }
    }



}

