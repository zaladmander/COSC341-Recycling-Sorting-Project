package com.example.cosc341_recycling_sorting_project.ui.schedule;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cosc341_recycling_sorting_project.R;

import java.util.Calendar;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class FragmentScheduleResult extends Fragment {

    private String upcomingPickupDay; // store temporarily while asking permission
    private ActivityResultLauncher<String> requestNotificationPermissionLauncher;

    public FragmentScheduleResult() {
        // required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the permission launcher early (onCreate)
        requestNotificationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Permission granted — schedule using the stored day
                        if (upcomingPickupDay != null) {
                            scheduleWeeklyPickupNotification(upcomingPickupDay);
                            Toast.makeText(requireContext(),
                                    "Notifications enabled — scheduled for " + upcomingPickupDay,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(),
                                    "Permission granted but no pickup day found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Permission denied
                        // If user selected "Don't ask again", we should direct them to settings
                        boolean showRationale = shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS);
                        if (!showRationale && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            // User permanently denied -> show guidance to open settings
                            Toast.makeText(requireContext(),
                                    "Notifications permanently denied. Enable them in app settings.",
                                    Toast.LENGTH_LONG).show();

                            // Optional: open app notification settings
                            Intent intent = new Intent();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                                intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());
                            } else {
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.fromParts("package", requireContext().getPackageName(), null));
                            }
                            startActivity(intent);
                        } else {
                            Toast.makeText(requireContext(),
                                    "Notifications denied. You can enable them later in settings.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_schedule_result, container, false);
        TextView txtSchedInfo = view.findViewById(R.id.txtSchedInfo);
        TextView txtSelectedHood = view.findViewById(R.id.txtSelectedHood);
        Button btnNotify = view.findViewById(R.id.btnNotify);

        // grab this weeks even or oddness (optional display)
        LocalDate today = null;
        WeekFields weekFields = null;
        int weekNumber = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            today = LocalDate.now();
            weekFields = WeekFields.of(Locale.getDefault());
            weekNumber = today.get(weekFields.weekOfWeekBasedYear());
        }
        boolean isEvenWeek = (weekNumber % 2 == 0);

        // receive the selected neighbourhood and place where necessary
        if (getArguments() != null) {
            String hood = getArguments().getString("selected_hood");
            String zone = getArguments().getString("selected_zone"); // e.g. "Monday A"
            String pickupDay = "Monday";
            if (zone != null && zone.contains(" ")) {
                pickupDay = zone.split(" ")[0];
            }

            txtSelectedHood.setText("Neighbourhood: " + (hood != null ? hood.toUpperCase() : "UNKNOWN"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && today != null) {
                if (isEvenWeek) {
                    txtSchedInfo.setText("According to your selected neighbourhood you are in Zone: "
                            + (zone != null ? zone.toUpperCase() : "UNKNOWN")
                            + "\nThis means for the week of " + today.toString()
                            + " you should put out your COMPOST and GARBAGE bins on " + pickupDay.toUpperCase() + ".");
                } else {
                    txtSchedInfo.setText("According to your selected neighbourhood you are in Zone: "
                            + (zone != null ? zone.toUpperCase() : "UNKNOWN")
                            + "\nThis means for the week of " + today.toString()
                            + " you should put out your RECYCLING and GARBAGE bins on " + pickupDay.toUpperCase() + ".");
                }
            } else {
                txtSchedInfo.setText("Zone info unavailable for display.");
            }

            // btnNotify listener uses the activity result launcher
            final String finalPickupDay = pickupDay;
            btnNotify.setOnClickListener(v -> {
                upcomingPickupDay = finalPickupDay;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // check if already granted
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                            != PackageManager.PERMISSION_GRANTED) {

                        // launch the modern permission request
                        requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);

                    } else {
                        // already granted
                        scheduleWeeklyPickupNotification(finalPickupDay);
                        Toast.makeText(requireContext(),
                                "Notifications scheduled for " + finalPickupDay,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // older devices: permission not required
                    scheduleWeeklyPickupNotification(finalPickupDay);
                    Toast.makeText(requireContext(),
                            "Notifications scheduled for " + finalPickupDay,
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // No args passed
            btnNotify.setOnClickListener(v ->
                    Toast.makeText(requireContext(), "No neighbourhood selected", Toast.LENGTH_SHORT).show()
            );
        }

        Button btnSeeFullSched = view.findViewById(R.id.btnSeeFullSched);
        btnSeeFullSched.setOnClickListener(v -> NavHostFragment.findNavController(FragmentScheduleResult.this)
                .navigate(R.id.action_schedule_result_to_full)
        );

        return view;
    }

    private void scheduleWeeklyPickupNotification(String pickupDay) {

        int targetDay = getCalendarDay(pickupDay);

        Calendar calendar = Calendar.getInstance();

        // Move to this week's target day
        while (calendar.get(Calendar.DAY_OF_WEEK) != targetDay) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Set to 7:00 AM
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // If the time is already past for today, move to next week
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        Intent intent = new Intent(requireContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                2000,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 7,
                    pendingIntent
            );
        } else {
            Toast.makeText(requireContext(), "Failed to obtain AlarmManager", Toast.LENGTH_SHORT).show();
        }
    }

    private int getCalendarDay(String day) {
        if (day == null) return Calendar.MONDAY;
        switch (day.toLowerCase(Locale.ROOT)) {
            case "monday":
                return Calendar.MONDAY;
            case "tuesday":
                return Calendar.TUESDAY;
            case "wednesday":
                return Calendar.WEDNESDAY;
            case "thursday":
                return Calendar.THURSDAY;
            case "friday":
                return Calendar.FRIDAY;
            case "saturday":
                return Calendar.SATURDAY;
            case "sunday":
                return Calendar.SUNDAY;
            default:
                return Calendar.MONDAY;
        }
    }
}
