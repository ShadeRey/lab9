package com.example.lb10;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lb10.R;
import com.example.lb10.databinding.FragmentContentBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ContentFragment extends Fragment {
    private FragmentContentBinding binding;
    private String filename = "file";

    public ContentFragment() {
        super(R.layout.fragment_content);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentContentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GetOrCreateDir();
        File dir = requireContext().getDir("texts", Context.MODE_PRIVATE);
        File file = new File(dir, filename);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                Scanner scanner = new Scanner(fis);
                scanner.useDelimiter("\\Z");
                String content = scanner.next();
                binding.editTextField.setText(content);
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        binding.editTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FileOutputStream fos = null;
                try {
                    File dir = requireContext().getDir("texts", Context.MODE_PRIVATE);
                    File file = new File(dir, filename);
                    fos = new FileOutputStream(file);
                    // fos = requireContext().openFileOutput("texts/" + filename, Context.MODE_PRIVATE);
                    fos.write(s.toString().getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void GetOrCreateDir() {
        File directory;
        Context context = requireContext();
        directory = context.getDir("texts", Context.MODE_PRIVATE);
        File[] files = directory.listFiles();
    }
}
