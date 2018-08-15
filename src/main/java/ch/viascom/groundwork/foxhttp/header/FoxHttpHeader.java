package ch.viascom.groundwork.foxhttp.header;

import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * FoxHttpHeader stores headers
 *
 * @author patrick.boesch@viascom.ch
 */
@Data
public class FoxHttpHeader implements Iterable<HeaderEntry>, Serializable {

    private List<HeaderEntry> headerEntries = new ArrayList<>();

    @Override
    public Iterator<HeaderEntry> iterator() {
        return headerEntries.iterator();
    }

    /**
     * Add a new header entry
     *
     * @param name name of the header entry
     * @param value value of the header entry
     */
    public void addHeader(String name, String value) {
        if (value != null) {
            headerEntries.add(new HeaderEntry(name, value));
        }
    }

    /**
     * Add a new header entry
     *
     * @param name name of the header entry
     * @param value value of the header entry
     */
    public void addHeader(HeaderTypes name, String value) {
        if (value != null) {
            headerEntries.add(new HeaderEntry(name.toString(), value));
        }
    }

    /**
     * Add a new map of header entries
     *
     * @param entries map of header entries
     */
    public void addHeader(Map<String, String> entries) {
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            if (entry.getValue() != null) {
                headerEntries.add(new HeaderEntry(entry.getKey(), entry.getValue()));
            }
        }
    }

    /**
     * Add a new array of header entries
     *
     * @param entries array of header entries
     */
    public void addHeader(List<HeaderEntry> entries) {
        headerEntries.addAll(entries);
    }

    /**
     * Get the first header based on its name
     *
     * @param name name of the header
     * @return a specific header
     */
    public HeaderEntry getHeader(String name) {
        for (HeaderEntry headerField : getHeaderEntries()) {
            if (headerField.getName().equals(name)) {
                return headerField;
            }
        }
        return null;
    }

    /**
     * Get all specific headers based on there name
     *
     * @param name name of the headers
     * @return all specific headers
     */
    public ArrayList<HeaderEntry> getHeaders(String name) {
        ArrayList<HeaderEntry> matchingList = new ArrayList<>();
        for (HeaderEntry headerEntry : getHeaderEntries()) {
            if (headerEntry.getName().equals(name)) {
                matchingList.add(headerEntry);
            }
        }
        return matchingList;
    }

    /**
     * Remove all headers by there names
     *
     * @param name name of the headers
     */
    public void removeHeader(String name) {
        headerEntries.removeIf(headerEntry -> headerEntry.getName().equals(name));
    }

    /**
     * Replace all headers by there name
     *
     * @param name name of the headers
     * @param value value of the replaced headers
     */
    public synchronized void replaceHeader(String name, String value) {
        ArrayList<HeaderEntry> replacedList = new ArrayList<>();
        headerEntries.forEach(headerEntry -> {
            if (headerEntry.getName().equals(name)) {
                HeaderEntry replacedEntry = new HeaderEntry(headerEntry.getName(), value);
                replacedList.add(replacedEntry);
            } else {
                replacedList.add(headerEntry);
            }
        });
        headerEntries = replacedList;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
