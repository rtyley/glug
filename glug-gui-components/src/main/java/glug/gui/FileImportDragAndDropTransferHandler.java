/**
 * 
 */
package glug.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.TransferHandler;

public abstract class FileImportDragAndDropTransferHandler extends TransferHandler {
	
	private static final long serialVersionUID = 1L;

	@Override
	public boolean canImport(TransferSupport support) {
		return extractUriListDataFlavour(support.getDataFlavors())!=null;
	}

	@Override
	public boolean importData(TransferSupport support) {
		try {
			DataFlavor uriListDataFlavour = extractUriListDataFlavour(support.getDataFlavors());
			Reader uriListReader=(Reader) support.getTransferable().getTransferData(uriListDataFlavour);
			List<File> files = convertUriList(uriListReader);
			System.out.println(files);
			load(files);
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public abstract void load(List<File> files);

	private List<File> convertUriList(Reader uriListReader) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(uriListReader);
		List<File> fileList = new ArrayList<File>();
		String line;
		while ((line=bufferedReader.readLine())!=null) {
			try {
				File file = new File(new URI(line));
				if (file.exists()) {
					fileList.add(file);
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		
		return fileList;
	}

	private DataFlavor extractUriListDataFlavour(DataFlavor[] dataFlavors) {
		for (DataFlavor dataFlavor : dataFlavors) {
			if (dataFlavor.isMimeTypeEqual("text/uri-list") && dataFlavor.getRepresentationClass().equals(Reader.class)) {
				return dataFlavor;
			}
		}
		return null;
	}
}