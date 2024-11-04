package cz.pycrs.hikeit.ui.goals;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import cz.pycrs.hikeit.GoalsAdapter;
import cz.pycrs.hikeit.R;
import cz.pycrs.hikeit.databinding.FragmentGoalsBinding;
import cz.pycrs.hikeit.ui.goals.picker.DatePickerFragment;
import cz.pycrs.hikeit.ui.goals.picker.TimePickerFragment;


public class GoalsFragment extends Fragment {
    private FragmentGoalsBinding binding;
    private GoalsViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.binding = FragmentGoalsBinding.inflate(inflater, container, false);
        this.viewModel = new ViewModelProvider(this).get(GoalsViewModel.class);

        View root = binding.getRoot();

        binding.goalsRecycler.setAdapter(new GoalsAdapter(viewModel.getGoals().getValue(), getResources(), viewModel.getApiService()));
        binding.goalsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        setupSpinner();
        setupPickers();
        setupRecycler();

        binding.buttonCreateGoal.setOnClickListener(v -> {
            String label = binding.textGoalLabel.getText().toString();
            String dateString = binding.textGoalDeadlineDate.getText().toString();
            String timeString = binding.textGoalDeadlineTime.getText().toString();
            String type = (String) binding.spinnerGoalType.getSelectedItem();
            String value = binding.textGoalValue.getText().toString();

            if (label.isEmpty()) binding.textGoalLabel.setError("Goal label is required");
            if (dateString.isEmpty()) binding.textGoalDeadlineDate.setError("Deadline date is required");
            if (timeString.isEmpty()) binding.textGoalDeadlineTime.setError("Deadline time is required");
            if (value.isEmpty()) binding.textGoalValue.setError("Goal value is required");

            try {
                LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalTime time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
                LocalDateTime localDateTime = LocalDateTime.of(date, time);

                if (localDateTime.isBefore(LocalDateTime.now()) || localDateTime.isEqual(LocalDateTime.now())) {
                    binding.textGoalDeadlineDate.setError("Deadline date must be in the future");
                    return;
                }

                viewModel.createGoal(
                        label,
                        localDateTime,
                        type,
                        Float.parseFloat(value)
                );
                Toast.makeText(getContext(), R.string.toast_goal_added, Toast.LENGTH_SHORT).show();
                // Clear the form
                binding.textGoalLabel.setText("");
                binding.textGoalDeadlineDate.setText("");
                binding.textGoalDeadlineTime.setText("");
                binding.textGoalValue.setText("");
            } catch(DateTimeParseException ignored) {
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void setupPickers() {
        binding.textGoalDeadlineDate.setOnClickListener(v -> {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getParentFragmentManager(), "datePicker");
        });

        binding.textGoalDeadlineTime.setOnClickListener(v -> {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getParentFragmentManager(), "dateTimePicker");
        });
    }
    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.goal_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGoalType.setAdapter(adapter);
    }

    private void setupRecycler() {
        viewModel.getGoals().observe(getViewLifecycleOwner(), goals -> {
            binding.goalsRecycler.setAdapter(new GoalsAdapter(goals, getResources(), viewModel.getApiService()));
        });
    }
}