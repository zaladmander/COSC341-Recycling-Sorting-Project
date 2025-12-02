package com.example.cosc341_recycling_sorting_project.ui.stats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cosc341_recycling_sorting_project.ui.identification.Recyclable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Small ViewModel that exposes isLoading and sample data.
 */
public class WeeklySummaryViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Double> kgs = new MutableLiveData<>(0.0);
    private final MutableLiveData<List<Recyclable>> items = new MutableLiveData<>(new ArrayList<>());

    // simple executor to simulate async loading
    private final Executor executor = Executors.newSingleThreadExecutor();

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Double> getKgs() { return kgs; }
    public LiveData<List<Recyclable>> getItems() { return items; }

    /**
     * Simulate fetching weekly summary from repository / network.
     * In a real app you would call your repository here (Room / Retrofit, etc.)
     */
    public void fetchWeeklySummary() {
        // if already loading, skip
        Boolean loading = isLoading.getValue();
        if (loading != null && loading) return;

        isLoading.setValue(true); // UI thread: mark loading

        executor.execute(() -> {
            try {
                // simulate network / DB latency
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                // ignore
            }

            // create sample data
            double fetchedKgs = 12.4;
            List<Recyclable> fetchedItems = new ArrayList<>();
            fetchedItems.add(new Recyclable("Coffee Cup", "Goes in Recycling Cart"));
            fetchedItems.add(new Recyclable("Glass Bottle", "Rinse first"));
            fetchedItems.add(new Recyclable("Aluminum Can", "Recyclable"));

            // post results back to LiveData (background thread -> use postValue)
            kgs.postValue(fetchedKgs);
            items.postValue(fetchedItems);

            // turn off loading
            isLoading.postValue(false);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}


