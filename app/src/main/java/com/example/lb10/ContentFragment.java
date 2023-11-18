package com.example.lb10;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
        binding.btnCreate.setOnClickListener(v -> {
            Context context = requireContext();
            EditText text = new EditText(context);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Name")
                    .setMessage("Введите название файла")
                    .setView(text)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            filename = text.getText().toString();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "Нажата кнопка 'Cancel'", Toast.LENGTH_SHORT).show();
                        }
                    });
            builder.create().show();
        });

        binding.btnOpen.setOnClickListener(v -> {
            Context context = requireContext();
            File dir = requireContext().getDir("texts", Context.MODE_PRIVATE);
            List<String> files = Arrays.stream(dir.listFiles()).filter(it -> it.isFile())
                    .map(it -> it.getName()).collect(Collectors.toList());
            String[] filenames = files.toArray(new String[0]);
            DialogInterface.OnClickListener aboba = (dialog, which) -> {
                if (which >= filenames.length) {
                    return;
                }
                filename = filenames[which];
            };
            new AlertDialog.Builder(context)
                    .setTitle("Select file")
                    .setSingleChoiceItems(filenames, 0, aboba)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
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
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "Нажата кнопка 'Cancel'", Toast.LENGTH_SHORT).show();
                        }
                    }).create().show();
        });

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
            } catch (Exception e) {
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
