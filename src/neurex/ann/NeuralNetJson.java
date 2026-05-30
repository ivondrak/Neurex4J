package neurex.ann;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class NeuralNetJson {
    private NeuralNetJson() {
    }

    public static NeuralNet read(Reader reader) throws IOException {
        Object parsed = new Parser(reader).parse();
        Map<String, Object> root = object(parsed, "root");

        List<Object> attributesList = array(root.get("attributes"), "attributes");
        if (attributesList.isEmpty()) {
            throw new IOException("Missing attributes");
        }
        Map<String, Object> attributesRoot = object(attributesList.getFirst(), "attributes[0]");
        Attribute[][] attributes = new Attribute[2][];
        attributes[0] = readAttributes(array(attributesRoot.get("input"), "attributes[0].input"));
        attributes[1] = readAttributes(array(attributesRoot.get("output"), "attributes[0].output"));

        Pattern[] patterns = readPatterns(array(root.get("patterns"), "patterns"));
        List<Object> neuronLayers = array(root.get("neurons"), "neurons");
        int hidden = neuronLayers.size() - 2;
        if (hidden < 1) {
            throw new IOException("Neural net must contain at least one hidden layer");
        }

        NeuralNet net = new NeuralNet(attributes, new TrainingSet(patterns), hidden);
        if (net.neurons.length != neuronLayers.size()) {
            throw new IOException("Neuron layer count does not match network topology");
        }
        readNeurons(net, neuronLayers);
        readConnections(net, array(root.get("connections"), "connections"));
        return net;
    }

    public static void write(NeuralNet net, Writer writer) throws IOException {
        JsonWriter json = new JsonWriter(writer);
        json.objectStart();
        json.name("neurons");
        writeNeurons(net, json);
        json.comma();
        json.name("connections");
        writeConnections(net, json);
        json.comma();
        json.name("patterns");
        writePatterns(net, json);
        json.comma();
        json.name("initialized");
        json.value(true);
        json.comma();
        json.name("attributes");
        writeAttributes(net, json);
        json.objectEnd();
        writer.write(System.lineSeparator());
    }

    private static Attribute[] readAttributes(List<Object> values) throws IOException {
        Attribute[] attributes = new Attribute[values.size()];
        for (int i = 0; i < values.size(); i++) {
            Map<String, Object> value = object(values.get(i), "attribute");
            attributes[i] = new Attribute(
                    string(value.get("attribute"), "attribute.attribute"),
                    number(value.get("minValue"), "attribute.minValue"),
                    number(value.get("maxValue"), "attribute.maxValue")
            );
        }
        return attributes;
    }

    private static Pattern[] readPatterns(List<Object> values) throws IOException {
        Pattern[] patterns = new Pattern[values.size()];
        for (int i = 0; i < values.size(); i++) {
            Map<String, Object> value = object(values.get(i), "patterns[" + i + "]");
            patterns[i] = new Pattern(
                    readDoubleArray(array(value.get("input"), "patterns[" + i + "].input")),
                    readDoubleArray(array(value.get("output"), "patterns[" + i + "].output"))
            );
        }
        return patterns;
    }

    private static void readNeurons(NeuralNet net, List<Object> layers) throws IOException {
        for (int i = 0; i < layers.size(); i++) {
            List<Object> values = array(object(layers.get(i), "neurons[" + i + "]").get("neurons"), "neurons[" + i + "].neurons");
            if (net.neurons[i].length != values.size()) {
                throw new IOException("Neuron count mismatch in layer " + i);
            }
            for (int j = 0; j < values.size(); j++) {
                Map<String, Object> value = object(values.get(j), "neurons[" + i + "].neurons[" + j + "]");
                Neuron neuron = net.neurons[i][j];
                neuron.delta = number(value.get("delta"), "neuron.delta");
                neuron.potential = number(value.get("potential"), "neuron.potential");
                neuron.state = number(value.get("state"), "neuron.state");
                neuron.slope = number(value.get("slope"), "neuron.slope");
                neuron.threshold = number(value.get("threshold"), "neuron.threshold");
            }
        }
    }

    private static void readConnections(NeuralNet net, List<Object> layers) throws IOException {
        if (net.layers.length != layers.size()) {
            throw new IOException("Connection layer count does not match network topology");
        }
        for (int i = 0; i < layers.size(); i++) {
            List<Object> rows = array(object(layers.get(i), "connections[" + i + "]").get("connections"), "connections[" + i + "].connections");
            if (net.layers[i].connections.length != rows.size()) {
                throw new IOException("Connection row count mismatch in layer " + i);
            }
            for (int row = 0; row < rows.size(); row++) {
                List<Object> weights = array(rows.get(row), "connections[" + i + "].connections[" + row + "]");
                if (net.layers[i].connections[row].length != weights.size()) {
                    throw new IOException("Connection column count mismatch in layer " + i + ", row " + row);
                }
                for (int col = 0; col < weights.size(); col++) {
                    net.layers[i].connections[row][col].weight = number(weights.get(col), "connection.weight");
                }
            }
        }
    }

    private static double[] readDoubleArray(List<Object> values) throws IOException {
        double[] result = new double[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = number(values.get(i), "number");
        }
        return result;
    }

    private static void writeNeurons(NeuralNet net, JsonWriter json) throws IOException {
        json.arrayStart();
        for (int i = 0; i < net.neurons.length; i++) {
            if (i > 0) {
                json.comma();
            }
            json.objectStart();
            json.name("neurons");
            json.arrayStart();
            for (int j = 0; j < net.neurons[i].length; j++) {
                if (j > 0) {
                    json.comma();
                }
                Neuron neuron = net.neurons[i][j];
                json.objectStart();
                json.name("delta");
                json.value(neuron.delta);
                json.comma();
                json.name("potential");
                json.value(neuron.potential);
                json.comma();
                json.name("state");
                json.value(neuron.state);
                json.comma();
                json.name("slope");
                json.value(neuron.slope);
                json.comma();
                json.name("threshold");
                json.value(neuron.threshold);
                json.objectEnd();
            }
            json.arrayEnd();
            json.objectEnd();
        }
        json.arrayEnd();
    }

    private static void writeConnections(NeuralNet net, JsonWriter json) throws IOException {
        json.arrayStart();
        for (int i = 0; i < net.layers.length; i++) {
            if (i > 0) {
                json.comma();
            }
            json.objectStart();
            json.name("connections");
            json.arrayStart();
            Connection[][] connections = net.layers[i].connections;
            for (int row = 0; row < connections.length; row++) {
                if (row > 0) {
                    json.comma();
                }
                json.arrayStart();
                for (int col = 0; col < connections[row].length; col++) {
                    if (col > 0) {
                        json.comma();
                    }
                    json.value(connections[row][col].weight);
                }
                json.arrayEnd();
            }
            json.arrayEnd();
            json.objectEnd();
        }
        json.arrayEnd();
    }

    private static void writePatterns(NeuralNet net, JsonWriter json) throws IOException {
        json.arrayStart();
        for (int i = 0; i < net.trainingSet.patterns.length; i++) {
            if (i > 0) {
                json.comma();
            }
            Pattern pattern = net.trainingSet.patterns[i];
            json.objectStart();
            json.name("input");
            writeDoubleArray(pattern.input, json);
            json.comma();
            json.name("output");
            writeDoubleArray(pattern.output, json);
            json.objectEnd();
        }
        json.arrayEnd();
    }

    private static void writeAttributes(NeuralNet net, JsonWriter json) throws IOException {
        json.arrayStart();
        json.objectStart();
        json.name("input");
        writeAttributeArray(net.attributes[0], json);
        json.comma();
        json.name("output");
        writeAttributeArray(net.attributes[1], json);
        json.objectEnd();
        json.arrayEnd();
    }

    private static void writeAttributeArray(Attribute[] attributes, JsonWriter json) throws IOException {
        json.arrayStart();
        for (int i = 0; i < attributes.length; i++) {
            if (i > 0) {
                json.comma();
            }
            Attribute attribute = attributes[i];
            json.objectStart();
            json.name("attribute");
            json.value(attribute.attribute);
            json.comma();
            json.name("maxValue");
            json.value(attribute.maxValue);
            json.comma();
            json.name("minValue");
            json.value(attribute.minValue);
            json.objectEnd();
        }
        json.arrayEnd();
    }

    private static void writeDoubleArray(double[] values, JsonWriter json) throws IOException {
        json.arrayStart();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                json.comma();
            }
            json.value(values[i]);
        }
        json.arrayEnd();
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> object(Object value, String name) throws IOException {
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        throw new IOException("Expected JSON object: " + name);
    }

    @SuppressWarnings("unchecked")
    private static List<Object> array(Object value, String name) throws IOException {
        if (value instanceof List<?> list) {
            return (List<Object>) list;
        }
        throw new IOException("Expected JSON array: " + name);
    }

    private static String string(Object value, String name) throws IOException {
        if (value instanceof String string) {
            return string;
        }
        throw new IOException("Expected JSON string: " + name);
    }

    private static double number(Object value, String name) throws IOException {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        throw new IOException("Expected JSON number: " + name);
    }

    private static final class JsonWriter {
        private final Writer writer;
        private int indent = 0;

        private JsonWriter(Writer writer) {
            this.writer = writer;
        }

        private void objectStart() throws IOException {
            writer.write("{");
            indent++;
        }

        private void objectEnd() throws IOException {
            indent--;
            newline();
            writer.write("}");
        }

        private void arrayStart() throws IOException {
            writer.write("[");
            indent++;
        }

        private void arrayEnd() throws IOException {
            indent--;
            newline();
            writer.write("]");
        }

        private void name(String name) throws IOException {
            newline();
            value(name);
            writer.write(" : ");
        }

        private void comma() throws IOException {
            writer.write(",");
        }

        private void value(String value) throws IOException {
            writer.write("\"");
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                switch (c) {
                    case '"' -> writer.write("\\\"");
                    case '\\' -> writer.write("\\\\");
                    case '\b' -> writer.write("\\b");
                    case '\f' -> writer.write("\\f");
                    case '\n' -> writer.write("\\n");
                    case '\r' -> writer.write("\\r");
                    case '\t' -> writer.write("\\t");
                    default -> {
                        if (c < 0x20) {
                            writer.write(String.format("\\u%04x", (int) c));
                        } else {
                            writer.write(c);
                        }
                    }
                }
            }
            writer.write("\"");
        }

        private void value(double value) throws IOException {
            if (!Double.isFinite(value)) {
                throw new IOException("JSON cannot represent non-finite numbers");
            }
            writer.write(Double.toString(value));
        }

        private void value(boolean value) throws IOException {
            writer.write(Boolean.toString(value));
        }

        private void newline() throws IOException {
            writer.write(System.lineSeparator());
            for (int i = 0; i < indent; i++) {
                writer.write("  ");
            }
        }
    }

    private static final class Parser {
        private final String json;
        private int index = 0;

        private Parser(Reader reader) throws IOException {
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[4096];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                builder.append(buffer, 0, read);
            }
            this.json = builder.toString();
        }

        private Object parse() throws IOException {
            Object value = parseValue();
            skipWhitespace();
            if (index != json.length()) {
                throw error("Unexpected trailing content");
            }
            return value;
        }

        private Object parseValue() throws IOException {
            skipWhitespace();
            if (index >= json.length()) {
                throw error("Unexpected end of JSON");
            }
            char c = json.charAt(index);
            return switch (c) {
                case '{' -> parseObject();
                case '[' -> parseArray();
                case '"' -> parseString();
                case 't' -> parseLiteral("true", Boolean.TRUE);
                case 'f' -> parseLiteral("false", Boolean.FALSE);
                case 'n' -> parseLiteral("null", null);
                default -> parseNumber();
            };
        }

        private Map<String, Object> parseObject() throws IOException {
            java.util.LinkedHashMap<String, Object> object = new java.util.LinkedHashMap<>();
            expect('{');
            skipWhitespace();
            if (peek('}')) {
                index++;
                return object;
            }
            while (true) {
                String key = parseString();
                skipWhitespace();
                expect(':');
                object.put(key, parseValue());
                skipWhitespace();
                if (peek('}')) {
                    index++;
                    return object;
                }
                expect(',');
            }
        }

        private List<Object> parseArray() throws IOException {
            List<Object> array = new ArrayList<>();
            expect('[');
            skipWhitespace();
            if (peek(']')) {
                index++;
                return array;
            }
            while (true) {
                array.add(parseValue());
                skipWhitespace();
                if (peek(']')) {
                    index++;
                    return array;
                }
                expect(',');
            }
        }

        private String parseString() throws IOException {
            expect('"');
            StringBuilder builder = new StringBuilder();
            while (index < json.length()) {
                char c = json.charAt(index++);
                if (c == '"') {
                    return builder.toString();
                }
                if (c == '\\') {
                    if (index >= json.length()) {
                        throw error("Unterminated escape sequence");
                    }
                    char escaped = json.charAt(index++);
                    switch (escaped) {
                        case '"' -> builder.append('"');
                        case '\\' -> builder.append('\\');
                        case '/' -> builder.append('/');
                        case 'b' -> builder.append('\b');
                        case 'f' -> builder.append('\f');
                        case 'n' -> builder.append('\n');
                        case 'r' -> builder.append('\r');
                        case 't' -> builder.append('\t');
                        case 'u' -> builder.append(parseUnicode());
                        default -> throw error("Invalid escape sequence");
                    }
                } else {
                    builder.append(c);
                }
            }
            throw error("Unterminated string");
        }

        private char parseUnicode() throws IOException {
            if (index + 4 > json.length()) {
                throw error("Invalid unicode escape");
            }
            try {
                char value = (char) Integer.parseInt(json.substring(index, index + 4), 16);
                index += 4;
                return value;
            } catch (NumberFormatException e) {
                throw error("Invalid unicode escape");
            }
        }

        private Object parseLiteral(String literal, Object value) throws IOException {
            if (!json.startsWith(literal, index)) {
                throw error("Invalid literal");
            }
            index += literal.length();
            return value;
        }

        private double parseNumber() throws IOException {
            int start = index;
            if (peek('-')) {
                index++;
            }
            consumeDigits();
            if (peek('.')) {
                index++;
                consumeDigits();
            }
            if (peek('e') || peek('E')) {
                index++;
                if (peek('+') || peek('-')) {
                    index++;
                }
                consumeDigits();
            }
            if (start == index) {
                throw error("Expected value");
            }
            try {
                return Double.parseDouble(json.substring(start, index));
            } catch (NumberFormatException e) {
                throw error("Invalid number");
            }
        }

        private void consumeDigits() {
            while (index < json.length() && Character.isDigit(json.charAt(index))) {
                index++;
            }
        }

        private void expect(char expected) throws IOException {
            skipWhitespace();
            if (!peek(expected)) {
                throw error("Expected '" + expected + "'");
            }
            index++;
        }

        private boolean peek(char c) {
            return index < json.length() && json.charAt(index) == c;
        }

        private void skipWhitespace() {
            while (index < json.length() && Character.isWhitespace(json.charAt(index))) {
                index++;
            }
        }

        private IOException error(String message) {
            return new IOException(message + " at JSON position " + index);
        }
    }
}
