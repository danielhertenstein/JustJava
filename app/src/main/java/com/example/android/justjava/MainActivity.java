package com.example.android.justjava;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText(String.valueOf(number));
    }

    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, R.string.too_many_coffees, Toast.LENGTH_SHORT).show();
            return;
        }
        quantity += 1;
        displayQuantity(quantity);
    }

    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, R.string.too_few_coffees, Toast.LENGTH_SHORT).show();
            return;
        }
        quantity -= 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText nameField = (EditText) findViewById(R.id.name_field);
        String name = nameField.getText().toString();

        boolean hasWhippedCream = ((CheckBox) findViewById(R.id.whipped_cream_checkbox)).isChecked();
        boolean hasChocolate = ((CheckBox) findViewById(R.id.chocolate_checkbox)).isChecked();
        int price = calculatePrice(hasWhippedCream, hasChocolate);

        String summaryMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, name);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, summaryMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     *
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int price = 5;
        if (addWhippedCream) { price += 1; }
        if (addChocolate) { price += 2; }
        return quantity * price;
    }

    /**
     * Creates the summary of the order based on quantity and price.
     */
    private String createOrderSummary(int priceOfOrder, boolean addWhippedCream, boolean addChocolate, String name) {
        String summary = getString(R.string.summary_name, name);
        summary += "\n" + getString(R.string.summary_whipped_cream, translateBoolean(addWhippedCream));
        summary += "\n" + getString(R.string.summary_chocolate, translateBoolean(addChocolate));
        summary += "\n" + getString(R.string.summary_quantity, quantity);
        summary += "\n" + getString(R.string.summary_total, priceOfOrder);
        summary += "\n" + getString(R.string.thank_you);
        return summary;
    }

    private String translateBoolean(boolean bool) {
        if (bool) { return getString(R.string.bool_true); }
        else { return getString(R.string.bool_false); }
    }
}
