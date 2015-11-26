package bit.nure.ua.cryptoapp;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Locale;

import ua.nure.bit.ciphers.CaesarCipher;
import ua.nure.bit.ciphers.additionals.Language;

public class MainActivity extends AppCompatActivity {
    NumberPicker np;
    EditText message;
    TextView result;
    Language language = Language.ENGLISH;
    ToggleButton toggleButton;
    ToggleButton languageButton;
    WebView webView;
    WebViewClient client = new CustomWebViewClient();
    Locale myLocale;

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
        languageButton = (ToggleButton) findViewById(R.id.languageButton);
        languageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLanguage();
                resetNumberPicket();
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

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(client);
        webView.loadUrl(getString(R.string.caesar_cypher_url));

    }

    private void resetNumberPicket() {
        np.setMinValue(1);
        np.setMaxValue(language.getAlphabetsAmount());
        np.setValue(3);
    }

    private void switchLanguage() {
        if (Language.ENGLISH == language) {
            language = Language.RUSSIAN;
        } else {
            language = Language.ENGLISH;
        }
    }

    private void makeCipher(String message, int offset) {
        String resultText;
        if (toggleButton.isChecked()) {
            resultText = new CaesarCipher(offset, language).encode(message);
        } else {
            resultText = new CaesarCipher(offset, language).decode(message);
        }
        result.setText(resultText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
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
        if (id == R.id.english) {
            setLocale("en");
        } else if (id == R.id.russian) {
            setLocale("ru");
        }

        return super.onOptionsItemSelected(item);
    }

    private void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        recreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null) {
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
