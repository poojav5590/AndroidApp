package edu.cmu.ds.pvasudev;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: Pooja Vasudevan
 * Last Modified: Apr 5, 2020
 *
 * This class takes the user search input string and makes a call to the search() method in GetFood to retrieve the necessary nutrient
 * information on click of the submit button. It contains the textReady() method that GetFood class accesses in order to display UI updates.
 *
 **/
public class FoodSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * The click listener will need a reference to this object, so that upon successfully finding nutritional information on the Food API, it
         * can callback to this object with the resulting textual information.  The "this" of the OnClick will be the OnClickListener, not
         * this FoodSearch object.
         */
        final FoodSearch ma = this;

        /*
         * Find the "submit" button, and add a listener to it
         */
        Button submitButton = (Button) findViewById(R.id.submit);




        // Add a listener to the submit button
        submitButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View viewParam) {
                String searchTerm = ((EditText) findViewById(R.id.searchTerm)).getText().toString();
                GetFood gp = new GetFood();
                gp.search(searchTerm, ma); // Done asynchronously in another thread.  It calls ip.pictureReady() in this thread when complete.
            }
        });
    }

    /**
     * This method takes the string result and parses through the result as a JSON Object. It extracts the relevant nutritional information
     * utilizing JSON methods and displays it on the UI through a TextView.
     * @void
     */
    public void textReady(String result) throws JSONException {
        ImageView pictureView = (ImageView) findViewById(R.id.staticImage);
        TextView searchView = (EditText)findViewById(R.id.searchTerm);
        TextView msgView = findViewById(R.id.msg);

        pictureView.setVisibility(View.VISIBLE);

        //if result exists display the text else inform user that search results don't exist.
        if (result.length()!=0) {
            msgView.setVisibility(View.VISIBLE);
            JSONObject nutrientObj = new JSONObject(result);


            msgView.setText("Here are the nutrient facts for "+searchView.getText().toString()+" per 100 g: \n"+
                   "Protein (g): "+ nutrientObj.getLong("PROCNT")+"\n"+
                    "Energy (kcal): "+ nutrientObj.getLong("ENERC_KCAL")+"\n"+
                    "Fat (g): "+ nutrientObj.getLong("FAT")+"\n"+
                    "Carbs (g): "+ nutrientObj.getLong("CHOCDF")+"\n"+
                    "Fiber (g): "+ nutrientObj.getLong("FIBTG")+"\n");




        } else {

            msgView.setVisibility(View.VISIBLE);
            msgView.setText("Sorry, no nutrient facts exist for "+searchView.getText().toString());

        }
        searchView.setText("");
    }
}
