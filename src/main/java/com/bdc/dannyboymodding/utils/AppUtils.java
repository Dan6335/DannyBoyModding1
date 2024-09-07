package com.bdc.dannyboymodding.utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AppUtils {
    public static String pastedCode;
    public static List<String> selectedEntries = new ArrayList<>();
    public static String selectedFileRegistryName;

    public static String convertToCamelCase(String text) {
        String[] words = text.split("_");
        StringBuilder camelCase = new StringBuilder();
        for (String word : words) {
            camelCase.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return camelCase.toString();
    }

    public static void createDinosaurClassesFromDirectory(String entitiesFolder, String outputPath, String classContent, String suffix) {
        File folder = new File(entitiesFolder);
        if (!folder.exists() || !folder.isDirectory()) return;

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".java"));
        if (files == null) return;

        for (File file : files) {
            String className = convertToCamelCase(file.getName().replace(".java", "")) + suffix;
            String classFilePath = outputPath + File.separator + className + ".java";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(classFilePath))) {
                writer.write(generateClassContent(className, classContent));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createDinosaurClassesFromNames(List<String> names, String outputPath, String classContent, String prefixSuffix) {
        File folder = new File(outputPath);
        if (!folder.exists() && !folder.mkdirs()) return;

        for (String name : names) {
            String combinedName;

            if (prefixSuffix != null && !prefixSuffix.isEmpty()) {
                // Determine if we are adding prefix or suffix
                if (prefixSuffix.startsWith("Prefix:")) {
                    combinedName = prefixSuffix.substring(7) + convertToCamelCase(name); // Remove "Prefix:" and prepend
                } else if (prefixSuffix.startsWith("Suffix:")) {
                    combinedName = convertToCamelCase(name) + prefixSuffix.substring(7); // Remove "Suffix:" and append
                } else {
                    combinedName = convertToCamelCase(name); // Default case if prefixSuffix is empty or unknown
                }
            } else {
                combinedName = convertToCamelCase(name);
            }

            String classFilePath = outputPath + File.separator + combinedName + ".java";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(classFilePath))) {
                writer.write(generateClassContent(combinedName, classContent));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyAndReplaceJavaFiles(String sourceDir, String outputDir, String content) {
        File sourceFolder = new File(sourceDir);
        File outputFolder = new File(outputDir);

        if (!sourceFolder.exists() || !sourceFolder.isDirectory()) return;
        if (!outputFolder.exists() && !outputFolder.mkdirs()) return;

        File[] files = sourceFolder.listFiles((dir, name) -> name.endsWith(".java"));
        if (files == null) return;

        for (File file : files) {
            String sourcePath = file.getPath();
            String outputPath = outputFolder.getPath() + File.separator + file.getName();

            try {
                Files.copy(file.toPath(), Paths.get(outputPath), StandardCopyOption.REPLACE_EXISTING);
                String fileContent = new String(Files.readAllBytes(Paths.get(outputPath)));
                fileContent = fileContent.replace("{class_name}", file.getName().replace(".java", ""));
                Files.write(Paths.get(outputPath), fileContent.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JOptionPane.showMessageDialog(null, "Files copied and replaced successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static String generateClassContent(String className, String classContent) {
        return classContent.replace("{class_name}", className);
    }

    public static void writeCodeToFile(String outputFilePath, int lineNumber, String code) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(outputFilePath));
            if (lineNumber < 0 || lineNumber > lines.size()) {
                System.out.println("Invalid line number");
                return;
            }

            lines.add(lineNumber, code);
            Files.write(Paths.get(outputFilePath), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getJurassicraftClassContent() {
        return Objects.requireNonNull(AppUtils.class.getResource("codes/JurassiCraftDinoCode.txt").toExternalForm());
    }

    public static String getBluedudeDragonsProjClassContent() {
        String content = Objects.requireNonNull(AppUtils.class.getResource("codes/BddDragonProjCode.txt").toExternalForm());

        // Define the pattern to find the placeholder in the return statement
        String placeholder = "return \"dragonType\";";
        String replacement = "return \"" + parseClassName(content) + "\";";

        // Replace the placeholder with the dynamic value
        if (content.contains(placeholder)) {
            return content.replace(placeholder, replacement);
        }

        return content;
    }

    public static String getBluedudeDragonsBreathClassContent() {
        String content = Objects.requireNonNull(AppUtils.class.getResource("codes/BddDragonBreathCode.txt").toExternalForm());

        // Define the pattern to find the placeholder in the return statement
        String placeholder = "return \"dragonType\";";
        String replacement = "return \"" + parseClassName(content) + "\";";

        // Replace the placeholder with the dynamic value
        if (content.contains(placeholder)) {
            return content.replace(placeholder, replacement);
        }

        return content;
    }

    public static String getCustomEntityClassContent(String customFilePath) {
        return readClassContentFromFile(customFilePath);
    }

    public static String getEntityRegistryTemplate(String type, String camelCaseName, String lowerCaseName, String upperCaseName) {
        // Define the template here or load it from a file/resource
        String template = "public static final RegistryObject<EntityType<{{camelCaseName}}>> {{UPPERCASE_NAME}} = JCRegistrate\n" +
                "        .entity(\"{{lowercase_name}}\", {{camelCaseName}}::new, MobCategory.CREATURE)\n" +
                "        .properties(builder -> builder.sized(1.4F, 2.0F)\n" +
                "                .clientTrackingRange(100)\n" +
                "                .setShouldReceiveVelocityUpdates(false))\n" +
                "        .spawnEgg(0x1D1F28, 0x1D1F27)\n" +
                "        .register();";

        // Replace placeholders in the template with actual values
        template = template.replace("{{camelCaseName}}", camelCaseName);
        template = template.replace("{{lowercase_name}}", lowerCaseName);
        template = template.replace("{{UPPERCASE_NAME}}", upperCaseName);

        return template;
    }

    public static String getBlockRegistryTemplate(String type, String camelCaseName, String lowerCaseName, String upperCaseName) {
        // Define the template here or load it from a file/resource
        String template = "public static final RegistryObject<Block> {{UPPERCASE_NAME}} = registerBlock(\"{{lowerCaseName}}\",\n" +
                "        () -> new {{camelCaseName}}(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()));";

        // Replace placeholders in the template with actual values
        template = template.replace("{{camelCaseName}}", camelCaseName);
        template = template.replace("{{lowercase_name}}", lowerCaseName);
        template = template.replace("{{UPPERCASE_NAME}}", upperCaseName);

        return template;
    }

    public static String getItemRegistryTemplate(String type, String camelCaseName, String lowerCaseName, String upperCaseName) {
        // Define the template here or load it from a file/resource
        String template = "public static final RegistryObject<Item> {{UPPERCASE_NAME}} = ITEMS.register(\"{{lowerCaseName}}\",\n" +
                "        () -> new Item(new Item.Properties()));";

        // Replace placeholders in the template with actual values
        template = template.replace("{{camelCaseName}}", camelCaseName);
        template = template.replace("{{lowercase_name}}", lowerCaseName);
        template = template.replace("{{UPPERCASE_NAME}}", upperCaseName);

        return template;
    }

    private static String readClassContentFromFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                contentBuilder.append(currentLine).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., log it or show an error message)
        }
        return contentBuilder.toString();
    }

    private static String parseClassName(String content) {
        String classNamePattern = "public class (\\w+) extends";
        Pattern pattern = Pattern.compile(classNamePattern);
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String className = matcher.group(1);
            String baseName = className.replace("Entity", "");
            String snakeCaseName = baseName.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
            return snakeCaseName;
        }
        return null;
    }

    public static void createItemFiles(List<String> names, String outputPath, Map<String, String> nameToTextureMap, String modId) {
        File folder = new File(outputPath);
        if (!folder.exists() && !folder.mkdirs()) {
            System.err.println("Failed to create output directory.");
            return;
        }

        for (String name : names) {
            String processedName = name.trim().toLowerCase().replaceAll(" ", "_");
            String jsonFilePath = outputPath + File.separator + processedName + ".json";

            // Determine the texture file for this name
            String textureFileName = nameToTextureMap.getOrDefault(processedName, "path_to_texture_file.png");

            // Remove .png extension from the texture file name
            String textureNameWithoutExtension = textureFileName.replace(".png", "");

            String fileContent = "{\n"
                    + "  \"parent\": \"item/generated\",\n"
                    + "  \"textures\": {\n"
                    + "    \"layer0\": \"" + modId + ":item/" + textureNameWithoutExtension + "\"\n"
                    + "  }\n"
                    + "}";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFilePath))) {
                writer.write(fileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }
        }
    }

    public static void createBasicBlockFiles(List<String> names, String outputPath, String modId) {
        String templateContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/BasicBlockCode.txt").toExternalForm());
        generateBlockFiles(names, outputPath, modId, templateContent);
    }


    public static void createLogColumnBlockFiles(List<String> names, String outputPath, String modId) {
        String verticalTemplateContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/LogColumnBlockCode.txt").toExternalForm());
        String horizontalTemplateContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/LogColumnHorizBlockCode.txt").toExternalForm());

        File folder = new File(outputPath);
        if (!folder.exists() && !folder.mkdirs()) {
            System.err.println("Failed to create output directory.");
            return;
        }

        for (String name : names) {
            String processedName = name.trim().toLowerCase().replaceAll(" ", "_");

            // Create the vertical log block JSON
            String verticalJsonFilePath = outputPath + File.separator + processedName + ".json";
            String verticalFileContent = verticalTemplateContent.replace("${MOD_ID}", modId)
                    .replace("${TEXTURE_NAME}", processedName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(verticalJsonFilePath))) {
                writer.write(verticalFileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }

            // Create the horizontal log block JSON
            String horizontalJsonFilePath = outputPath + File.separator + processedName + "_horizontal.json";
            String horizontalFileContent = horizontalTemplateContent.replace("${MOD_ID}", modId)
                    .replace("${TEXTURE_NAME}", processedName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(horizontalJsonFilePath))) {
                writer.write(horizontalFileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }
        }
    }

    public static void createFlowerBlockFiles(List<String> names, String outputPath, String modId) {
        String templateContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/FlowerBlockCode.txt").toExternalForm());

        generateBlockFiles(names, outputPath, modId, templateContent);
    }

    public static void createStairsBlockFiles(List<String> names, String outputPath, String modId) {
        String fenceContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/StairsBlockCode.txt").toExternalForm());
        String stairsInnerContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/StairsInnerBlockCode.txt").toExternalForm());
        String stairsOuterContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/StairsOuterBlockCode.txt").toExternalForm());

        File folder = new File(outputPath);
        if (!folder.exists() && !folder.mkdirs()) {
            System.err.println("Failed to create output directory.");
            return;
        }

        for (String name : names) {
            String processedName = name.trim().toLowerCase().replaceAll(" ", "_");

            // Create the vertical log block JSON
            String verticalJsonFilePath = outputPath + File.separator + processedName + ".json";
            String verticalFileContent = fenceContent.replace("${MOD_ID}", modId)
                    .replace("${TEXTURE_NAME}", processedName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(verticalJsonFilePath))) {
                writer.write(verticalFileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }

            // Create the horizontal log block JSON
            String innerJsonFilePath = outputPath + File.separator + processedName + "_inner.json";
            String innerFileContent = stairsInnerContent.replace("${MOD_ID}", modId)
                    .replace("${TEXTURE_NAME}", processedName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(innerJsonFilePath))) {
                writer.write(innerFileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }

            // Create the horizontal log block JSON
            String outerJsonFilePath = outputPath + File.separator + processedName + "_outer.json";
            String outerFileContent = stairsOuterContent.replace("${MOD_ID}", modId)
                    .replace("${TEXTURE_NAME}", processedName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outerJsonFilePath))) {
                writer.write(outerFileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }
        }
    }

    public static void createFenceBlockFiles(List<String> names, String outputPath, String modId) {
        String fenceContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/FenceBlockCode.txt")).toExternalForm();
        String fencePostContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/FencePostBlockCode.txt").toExternalForm());
        String fenceSideContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/FenceSideBlockCode.txt").toExternalForm());
        String fenceGateInvContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/FenceInvBlockCode.txt").toExternalForm());

        File folder = new File(outputPath);
        if (!folder.exists() && !folder.mkdirs()) {
            System.err.println("Failed to create output directory.");
            return;
        }

        for (String name : names) {
            String processedName = name.trim().toLowerCase().replaceAll(" ", "_");

            // Create the vertical log block JSON
            String verticalJsonFilePath = outputPath + File.separator + processedName + ".json";
            String verticalFileContent = fenceContent.replace("${MOD_ID}", modId)
                    .replace("${TEXTURE_NAME}", processedName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(verticalJsonFilePath))) {
                writer.write(verticalFileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }

            // Create the horizontal log block JSON
            String horizontalJsonFilePath = outputPath + File.separator + processedName + "_post.json";
            String horizontalFileContent = fencePostContent.replace("${MOD_ID}", modId)
                    .replace("${TEXTURE_NAME}", processedName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(horizontalJsonFilePath))) {
                writer.write(horizontalFileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }

            // Create the horizontal log block JSON
            String sideJsonFilePath = outputPath + File.separator + processedName + "_side.json";
            String sideFileContent = fenceSideContent.replace("${MOD_ID}", modId)
                    .replace("${TEXTURE_NAME}", processedName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(sideJsonFilePath))) {
                writer.write(sideFileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }

            // Create the horizontal log block JSON
            String invJsonFilePath = outputPath + File.separator + processedName + "_inventory.json";
            String invFileContent = fenceGateInvContent.replace("${MOD_ID}", modId)
                    .replace("${TEXTURE_NAME}", processedName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(invJsonFilePath))) {
                writer.write(invFileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }
        }
    }

    public static void createSlabBlockFiles(List<String> names, String outputPath, String modId) {
        String slabBottomContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/SlabTopBlockCode.txt").toExternalForm());
        String slabTopContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/SlabBlockCode.txt").toExternalForm());

        File folder = new File(outputPath);
        if (!folder.exists() && !folder.mkdirs()) {
            System.err.println("Failed to create output directory.");
            return;
        }

        for (String name : names) {
            String processedName = name.trim().toLowerCase().replaceAll(" ", "_");

            // Create the vertical log block JSON
            String verticalJsonFilePath = outputPath + File.separator + processedName + ".json";
            String verticalFileContent = slabBottomContent.replace("${MOD_ID}", modId)
                    .replace("${TEXTURE_NAME}", processedName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(verticalJsonFilePath))) {
                writer.write(verticalFileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }

            // Create the horizontal log block JSON
            String horizontalJsonFilePath = outputPath + File.separator + processedName + "_top.json";
            String horizontalFileContent = slabTopContent.replace("${MOD_ID}", modId)
                    .replace("${TEXTURE_NAME}", processedName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(horizontalJsonFilePath))) {
                writer.write(horizontalFileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }
        }
    }

    public static void createFenceGateBlockFiles(List<String> names, String outputPath, String modId) {
        String fenceContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/FenceGateBlockCode.txt").toExternalForm());
        String fenceGateOpenContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/FenceGateOpenBlockCode.txt").toExternalForm());
        String fenceGateWallContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/FenceGateWallBlockCode.txt").toExternalForm());

        File folder = new File(outputPath);
        if (!folder.exists() && !folder.mkdirs()) {
            System.err.println("Failed to create output directory.");
            return;
        }

        for (String name : names) {
            String processedName = name.trim().toLowerCase().replaceAll(" ", "_");

            // Create the vertical log block JSON
            String verticalJsonFilePath = outputPath + File.separator + processedName + ".json";
            String verticalFileContent = fenceContent.replace("${MOD_ID}", modId)
                    .replace("${TEXTURE_NAME}", processedName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(verticalJsonFilePath))) {
                writer.write(verticalFileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }

            // Create the horizontal log block JSON
            String horizontalJsonFilePath = outputPath + File.separator + processedName + "_open.json";
            String horizontalFileContent = fenceGateOpenContent.replace("${MOD_ID}", modId)
                    .replace("${TEXTURE_NAME}", processedName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(horizontalJsonFilePath))) {
                writer.write(horizontalFileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }

            // Create the horizontal log block JSON
            String sideJsonFilePath = outputPath + File.separator + processedName + "_wall.json";
            String sideFileContent = fenceGateWallContent.replace("${MOD_ID}", modId)
                    .replace("${TEXTURE_NAME}", processedName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(sideJsonFilePath))) {
                writer.write(sideFileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }
        }
    }

    public static void createGrassBlockFiles(List<String> names, String outputPath, String modId) {
        //String mainFolder = System.getProperty("user.dir");
        String templateContent = Objects.requireNonNull(AppUtils.class.getResource("codes/blocks/GrassBlockCode.txt").toExternalForm());

        generateBlockFiles(names, outputPath, modId, templateContent);
    }

    private static void generateBlockFiles(List<String> names, String outputPath, String modId, String templateContent) {
        File folder = new File(outputPath);
        if (!folder.exists() && !folder.mkdirs()) {
            System.err.println("Failed to create output directory.");
            return;
        }

        for (String name : names) {
            String processedName = name.trim().toLowerCase().replaceAll(" ", "_");
            String jsonFilePath = outputPath + File.separator + processedName + ".json";

            // Replace placeholders in the template content with actual values
            String fileContent = templateContent.replace("${MOD_ID}", modId)
                    .replace("${TEXTURE_NAME}", processedName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFilePath))) {
                writer.write(fileContent);
            } catch (IOException ex) {
                System.err.println("Failed to create file: " + ex.getMessage());
            }
        }
    }

    public static List<String> extractMobAttributesFromSelection(String type) throws IOException {
        List<String> attributes = new ArrayList<>();
        for (String entry : selectedEntries) {
            String[] parts = entry.split(" - ");
            String registryName = parts[0].trim();
            String className = parts[1].trim();

            // Include the registry name in the attribute line
            String attributeLine = "event.put(" + selectedFileRegistryName + "." + registryName + ".get(), " + className +
                    (type.equals("Bluedude Dragons Attributes") ? ".bakeAttributes().build());" : ".createAttributes().build());");
            attributes.add(attributeLine);
        }
        return attributes;
    }

    public static void setSelectedFileRegistryName(String fileName) {
        // Remove the .java extension and set the file name as the registry name
        selectedFileRegistryName = fileName.replace(".java", "");
    }

    public static void generateAttributesFile(List<String> attributes, File outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String attribute : attributes) {
                writer.write(attribute);
                writer.newLine();
            }
        }
    }

    public static void openEntrySelectionDialog(File inputFile) {
        JDialog dialog = new JDialog((JFrame) null, "Select Entries", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);

        // Panel for checkboxes
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
        Map<String, JCheckBox> checkBoxMap = new HashMap<>();

        // Method to load entries and update checkboxes
        Consumer<File> loadEntries = file -> {
            checkBoxMap.clear();
            checkBoxPanel.removeAll();

            List<String> entries = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                Pattern pattern = Pattern.compile("public static final RegistryObject<EntityType<([\\w]+)>> (\\w+) =");

                while ((line = reader.readLine()) != null) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        String className = matcher.group(1);
                        String registryName = matcher.group(2);
                        entries.add(registryName + " - " + className);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error reading file.");
                return;
            }

            // Add checkboxes for each entry
            for (String entry : entries) {
                JCheckBox checkBox = new JCheckBox(entry);
                checkBoxMap.put(entry, checkBox);
                checkBoxPanel.add(checkBox);
            }

            checkBoxPanel.revalidate();
            checkBoxPanel.repaint();
        };

        // Initial load of entries
        loadEntries.accept(inputFile);

        // Add components to the dialog
        dialog.add(new JScrollPane(checkBoxPanel), BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();

        // OK button
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            AppUtils.selectedEntries = checkBoxMap.entrySet().stream()
                    .filter(entry -> entry.getValue().isSelected())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            dialog.dispose();
        });
        buttonPanel.add(okButton);

        // Change File button
        JButton changeFileButton = new JButton("Change File");
        changeFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Java and Text Files", "java", "txt"));
            int returnValue = fileChooser.showOpenDialog(dialog);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File newFile = fileChooser.getSelectedFile();
                loadEntries.accept(newFile);
            }
        });
        buttonPanel.add(changeFileButton);

        // Select All button
        JButton selectAllButton = new JButton("Select All");
        selectAllButton.addActionListener(e -> {
            boolean allSelected = checkBoxMap.values().stream().allMatch(JCheckBox::isSelected);
            boolean select = !allSelected; // Toggle selection state

            for (JCheckBox checkBox : checkBoxMap.values()) {
                checkBox.setSelected(select);
            }
        });
        buttonPanel.add(selectAllButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    /**
     * Converts a given string to CamelCase format.
     * Example: "cookie monster" -> "CookieMonster"
     *
     * @param input The input string.
     * @return The CamelCase formatted string.
     */
    public static String toCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder camelCaseString = new StringBuilder();
        boolean nextCharUpperCase = false;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c) || c == '_') {
                nextCharUpperCase = true; // Next character should be uppercase
            } else if (nextCharUpperCase) {
                camelCaseString.append(Character.toUpperCase(c));
                nextCharUpperCase = false; // Reset flag after adding uppercase
            } else {
                camelCaseString.append(Character.toLowerCase(c));
            }
        }

        // Ensure the first character is lowercase
        if (camelCaseString.length() > 0) {
            camelCaseString.setCharAt(0, Character.toLowerCase(camelCaseString.charAt(0)));
        }

        return camelCaseString.toString();
    }

    public static boolean isCamelCase(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        // Check if it starts with a lowercase letter and only contains camel case pattern
        return input.matches("^[a-z][a-zA-Z0-9]*$");
    }

    /**
     * Writes the provided content to a specified file.
     *
     * @param file    The file to write to.
     * @param content The content to be written.
     * @throws IOException If an I/O error occurs.
     */
    public static void writeToFile(File file, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }

    /**
     * Converts a given string to lowercase with underscores.
     * Example: "Cookie Monster" -> "cookie_monster"
     *
     * @param input The input string.
     * @return The lowercase string with underscores.
     */
    public static String toLowerCaseWithUnderscores(String input) {
        return input.trim().toLowerCase().replaceAll("\\s+", "_");
    }

    /**
     * Converts a given string to uppercase with underscores.
     * Example: "Cookie Monster" -> "COOKIE_MONSTER"
     *
     * @param input The input string.
     * @return The uppercase string with underscores.
     */
    public static String toUpperCaseWithUnderscores(String input) {
        return input.trim().toUpperCase().replaceAll("\\s+", "_");
    }

    public static void setComponentColors(Color background, Color foreground, JComponent... components) {
        for (JComponent component : components) {
            component.setBackground(background);
            component.setForeground(foreground);
        }
    }

    public static void setTabColors(JRadioButton button, Color selectedColor, Color unselectedColor) {
        button.addItemListener(e -> {
            if (button.isSelected()) {
                button.setBackground(selectedColor);
                button.setForeground(Color.WHITE); // Assuming white text on selected color
            } else {
                button.setBackground(unselectedColor);
                button.setForeground(Color.BLACK); // Assuming black text on unselected color
            }
        });
    }
}