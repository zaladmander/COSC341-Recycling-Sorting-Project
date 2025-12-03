package com.example.cosc341_recycling_sorting_project.ui.stats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cosc341_recycling_sorting_project.R;
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
            fetchedItems.add(new Recyclable("Aerosol Cans", "Completely empty aerosol cans for food, deodorant, hairspray, shaving cream and air freshener. Return partially full or paint aerosols to a hazardous waste program.", R.drawable.aerosol_cans));
            fetchedItems.add(new Recyclable("Glass Jars", "Clear or coloured glass food jars and bottles that do not carry a beverage deposit. Empty, rinse and remove lids.", R.drawable.glass_jars_bottles));
            fetchedItems.add(new Recyclable("Newspaper flyers", "Clean newspapers, community papers and advertising flyers. Recycle loose with other paper.", R.drawable.newspapers_flyers));

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


