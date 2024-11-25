package com.example.trainiglistapp;

import android.os.Bundle;
import android.view.View;
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
    private List<Exercise> exerciseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exerciseList = new ArrayList<>();
        loadInitialExercises();

        adapter = new ExerciseAdapter(exerciseList);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExercise("Nuevo Ejercicio", "Fuerza", "Principiante");
                Snackbar snackbar = Snackbar.make(view, "Ejercicio añadido", Snackbar.LENGTH_LONG)
                        .setAction("Deshacer", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                removeLastExercise();
                            }
                        });
                snackbar.show();
            }
        });
    }

    private void loadInitialExercises() {
        exerciseList.add(new Exercise("Sentadillas", "Fuerza", "Intermedio"));
        exerciseList.add(new Exercise("Flexiones", "Fuerza", "Principiante"));
        exerciseList.add(new Exercise("Plancha", "Core", "Avanzado"));
    }

    private void addExercise(String name, String type, String level) {
        exerciseList.add(new Exercise(name, type, level));
        adapter.notifyItemInserted(exerciseList.size() - 1);
    }

    private void removeLastExercise() {
        if (!exerciseList.isEmpty()) {
            int lastIndex = exerciseList.size() - 1;
            exerciseList.remove(lastIndex);
            adapter.notifyItemRemoved(lastIndex);
        }
    }
}