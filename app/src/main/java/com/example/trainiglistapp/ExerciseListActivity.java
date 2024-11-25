package com.example.trainiglistapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ExerciseListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private List<Exercise> exerciseList, filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exerciseList = new ArrayList<>();
        loadInitialExercises();
        filteredList = new ArrayList<>(exerciseList);

        adapter = new ExerciseAdapter(filteredList);
        recyclerView.setAdapter(adapter);

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

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(view -> {
            addExercise();
            Snackbar snackbar = Snackbar.make(view, "Ejercicio añadido", Snackbar.LENGTH_LONG)
                    .setAction("Deshacer", v -> removeLastExercise());
            snackbar.show();
        });
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
        exerciseList.add(new Exercise("Sentadillas", "Fuerza", "Intermedio"));
        exerciseList.add(new Exercise("Flexiones", "Fuerza", "Principiante"));
        exerciseList.add(new Exercise("Plancha", "Core", "Avanzado"));
    }

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
}
