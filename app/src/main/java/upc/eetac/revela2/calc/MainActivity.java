package upc.eetac.revela2.calc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

import upc.eetac.revela2.calc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private List<Button> buttons;
    private String input = "0";
    private String operator = "";
    private double res = 0;
    private boolean newop = false;
    private boolean isradians = false;
    private boolean resetInputOnNextDigit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttons = Arrays.asList(
                binding.button0,
                binding.button,
                binding.button2,
                binding.button3,
                binding.button4,
                binding.button5,
                binding.button6,
                binding.button7,
                binding.button8,
                binding.button9,
                binding.buttonC,
                binding.buttonplus2,
                binding.buttonminus,
                binding.buttonmult,
                binding.buttondivide,
                binding.buttonsin,
                binding.buttoncos,
                binding.buttontan,
                binding.buttonequals,
                binding.buttondecimal
        );

        binding.displayView.setText(this.input);
        binding.radView.setText(isradians ? "Rad" : "Deg");
    }

    public void onClickNumber(View v) {
        Button btn = (Button) v;
        if (this.input.equals("0") || resetInputOnNextDigit) {
            this.input = btn.getText().toString();
            resetInputOnNextDigit = false;
        } else {
            this.input += btn.getText().toString();
        }
        binding.displayView.setText(this.input);
    }

    private void prepareOperation(String op) {
        if (!this.newop) {
            this.res = Double.parseDouble(this.input);
            this.newop = true;
        } else {
            calculate();
        }
        this.operator = op;
        this.resetInputOnNextDigit = true;
    }

    public void clickPlus(View v) {
        prepareOperation("+");
    }

    public void clickMinus(View v) {
        prepareOperation("-");
    }

    public void clickMult(View v) {
        prepareOperation("*");
    }

    public void clickDivide(View v) {
        prepareOperation("/");
    }

    public void clickC(View v) {
        if (this.input.equals("0")) return;

        this.input = this.input.substring(0, this.input.length() - 1);
        if (this.input.isEmpty() || this.input.equals("-")) {
            this.input = "0";
        }
        binding.displayView.setText(this.input);
    }

    public void clickAc(View v) {
        this.operator = "";
        this.input = "0";
        this.newop = false;
        this.resetInputOnNextDigit = false;
        this.res = 0;
        binding.displayView.setText("0");
        buttons.forEach(btn -> btn.setEnabled(true));
    }

    public void clickDeg(View v) {
        this.isradians = !this.isradians;
        binding.radView.setText(isradians ? "Rad" : "Deg");
    }

    public void clickTrig(View v) {
        double value = Double.parseDouble(this.input);
        double angle = isradians ? value : ((value * Math.PI) / 180);

        Button btn = (Button) v;
        switch (btn.getText().toString().toLowerCase()) {
            case "sin":
                this.res = Math.sin(angle);
                break;
            case "cos":
                this.res = Math.cos(angle);
                break;
            case "tan":
                if (Math.abs(Math.cos(angle)) < 1e-15) {
                    binding.displayView.setText("Undefined");
                    buttons.forEach(b -> b.setEnabled(false));
                    return;
                }
                this.res = Math.tan(angle);
                break;
            default:
                binding.displayView.setText("Err");
                buttons.forEach(b -> b.setEnabled(false));
                return;
        }
        this.input = String.valueOf(this.res);
        binding.displayView.setText(this.input);
        this.resetInputOnNextDigit = true;
    }

    public void clickDecimal(View v) {
        if (resetInputOnNextDigit) {
            this.input = "0.";
            resetInputOnNextDigit = false;
        } else if (!this.input.contains(".")) {
            this.input += ".";
        }
        binding.displayView.setText(this.input);
    }

    public void clickEquals(View v) {
        if (this.operator.isEmpty()) return;

        calculate();
        this.operator = "";
        this.newop = false;
        this.resetInputOnNextDigit = true;
    }

    private void calculate() {
        double currentInput = Double.parseDouble(this.input);
        switch (this.operator) {
            case "+":
                this.res += currentInput;
                break;
            case "-":
                this.res -= currentInput;
                break;
            case "*":
                this.res *= currentInput;
                break;
            case "/":
                if (currentInput == 0.0) {
                    binding.displayView.setText("Cannot divide by 0");
                    buttons.forEach(btn -> btn.setEnabled(false));
                    return;
                }
                this.res /= currentInput;
                break;
            default:
                binding.displayView.setText("Err");
                buttons.forEach(b -> b.setEnabled(false));
                return;
        }
        this.input = String.valueOf(this.res);
        binding.displayView.setText(this.input);
    }
}