/*
 * Copyright © 2017 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p1;

import java.io.IOException;
import java.util.*;

import javafx.application.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import lejos.robotics.filter.*;

public class PcMainFx extends Application
{
	public static void main(String[] args)
	{
		data = FXCollections.observableArrayList();
		new Thread(PcMainFx::pullData).start();
		launch(args);
	}
	
	private static ObservableList<Data> data;
	
	private static void pullData()
	{
		while(true)
		{
			Collection<PublishedSource> sources = PublishedSource.getSources();
			for(PublishedSource s : sources)
			{
				try
				{
					Data d = new Data(s);
					Platform.runLater(() -> data.add(d));
				}
				catch(IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@FXML
	private ListView<Data> lstSamples;
	@FXML
	private ListView<String> lstData;
	
	private ObservableList<String> sampleData;
	
	@Override
	public void start(Stage stage) throws Exception
	{
		FXMLLoader l = new FXMLLoader(getClass().getResource("Gui.fxml"));
		l.setController(this);
		stage.setScene(new Scene(l.load()));
		stage.setTitle("KI-data view");
		stage.setOnHidden(e -> System.exit(0));
		stage.show();
	}
	
	@FXML
	private void initialize()
	{
		sampleData = FXCollections.observableArrayList();
		
		lstSamples.setItems(data);
		lstData.setItems(sampleData);
		
		lstSamples.setCellFactory(new Callback<ListView<Data>, ListCell<Data>>()
		{
			
			@Override
			public ListCell<Data> call(ListView<Data> param)
			{
				return new ListCell<Data>()
					{
						@Override
						protected void updateItem(Data item, boolean empty)
						{
							super.updateItem(item, empty);
							
							if(empty || item == null)
							{
								setText("");
							}
							else
							{
								setText(
									String.format(
										"%s (%s/%d) [%s]",
										item.host,
										item.ip,
										item.port,
										item.last));
							}
						}
					};
			}
		});
		
		lstSamples.getSelectionModel().selectedItemProperty().addListener(
			(p, o, n) -> newSelection());
	}
	
	private void newSelection()
	{
		if(lstSamples.getSelectionModel().getSelectedItem() == null)
		{
			sampleData.clear();
		}
		else
		{
			Data d = lstSamples.getSelectionModel().getSelectedItem();
			List<String> l = new ArrayList<>(d.sample.length + 7);
			
			l.add(String.format("IP: %s", d.ip));
			l.add(String.format("Host: %s", d.host));
			l.add(String.format("Port: %d", d.port));
			l.add(String.format("Name: %s", d.name));
			l.add(String.format("Size: %d", d.size));
			l.add(String.format("Freq: %.2f", d.freq));
			l.add(String.format("Last: %s", d.last));
			
			for(int i = 0; i < d.sample.length; ++i)
			{
				l.add(String.format("Sample-%d: %.2f", i, d.sample[i]));
			}
			
			sampleData.setAll(l);
		}
	}
	
	private static class Data
	{
		public final String ip;
		public final String host;
		public final int port;
		public final String name;
		public final int size;
		public final float freq;
		public final String last;
		public final float[] sample;

		public Data(String ip, String host, int port, String name, int size,
					float freq, String last, float[] sample)
		{
			this.ip = ip;
			this.host = host;
			this.port = port;
			this.name = name;
			this.size = size;
			this.freq = freq;
			this.last = last;
			this.sample = sample;
		}

		public Data(PublishedSource s) throws IOException
		{
			ip = s.getIpAddress();
			host = s.getHost();
			port = s.getPort();
			name = s.getName();
			size = s.sampleSize();
			freq = s.getFrequency();
			last = s.getTime();
			
			SubscribedProvider provider = s.connect();
			sample = new float[s.sampleSize()];
			
			provider.fetchSample(sample, 0);
			
			s.close();
		}
	}
}
