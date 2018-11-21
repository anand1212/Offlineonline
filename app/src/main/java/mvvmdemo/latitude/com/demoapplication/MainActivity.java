package mvvmdemo.latitude.com.demoapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EmployeeAdapter adapter;
    private RecyclerView recyclerView;
    DataBaseHelper dataBaseHelper;
    ArrayList<Employee> employees = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataBaseHelper = new DataBaseHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_employee_list);
        if (connectionAvailable()) {
            GetEmployeeDataService service = RetrofitInstance.getRetrofitInstance().create(GetEmployeeDataService.class);

            /*Call the method with parameter in the interface to get the employee data*/
            Call<EmployeeList> call = service.getEmployeeData(100);

            /*Log the URL called*/
            Log.wtf("URL Called", call.request().url() + "");

            call.enqueue(new Callback<EmployeeList>() {
                @Override
                public void onResponse(Call<EmployeeList> call, Response<EmployeeList> response) {
                    generateEmployeeList(response.body().getEmployeeArrayList());
                }

                @Override
                public void onFailure(Call<EmployeeList> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            employees = dataBaseHelper.getAllStudentsList();
            adapter = new EmployeeAdapter(employees);


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);

            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(adapter);
        }
        /*Create handle for the RetrofitInstance interface*/

    }

    /*Method to generate List of employees using RecyclerView with custom adapter*/
    private void generateEmployeeList(ArrayList<Employee> empDataList) {
//        empDataList.clear();
        employees.clear();
        employees.addAll(empDataList);
        adapter = new EmployeeAdapter(empDataList);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
        for (int i = 0; i < empDataList.size(); i++) {
            dataBaseHelper.addContacts(employees.get(i).getName(), employees.get(i).getEmail(), employees.get(i).getPhone());
        }
    }

    private boolean connectionAvailable() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        return connected;
    }
}
