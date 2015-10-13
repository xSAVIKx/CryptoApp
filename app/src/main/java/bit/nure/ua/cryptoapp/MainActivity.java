package bit.nure.ua.cryptoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ToggleButton;

import ua.nure.bit.ciphers.CaesarCipher;
import ua.nure.bit.ciphers.additionals.Language;

public class MainActivity extends AppCompatActivity {

    NumberPicker np;
    EditText message;
    TextView result;
    Language language = Language.ENGLISH;
    ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCipher(message.getText().toString(), np.getValue());
            }
        });
        np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setMinValue(1);
        np.setMaxValue(language.getAlphabetsAmount());
        np.setValue(3);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                makeCipher(message.getText().toString(), newVal);
            }
        });
        message = (EditText) findViewById(R.id.message);
        result = (TextView) findViewById(R.id.crypto);
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                makeCipher(s.toString(), np.getValue());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void makeCipher(String message, int offset) {
        String resultText;
        if (toggleButton.isChecked()) {
            resultText = new CaesarCipher(offset).encode(message);
        } else {
            resultText = new CaesarCipher(offset).decode(message);
        }
        result.setText(resultText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
