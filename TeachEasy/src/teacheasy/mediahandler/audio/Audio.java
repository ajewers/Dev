/*
 * Alex Cash
 * 
 * Copyright (c) 2015 Sofia Software Solutions. All Rights Reserved.
 * 
 */
package teacheasy.mediahandler.audio;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This class is describes an individual audio track 
 * that will be handled by the audio handler
 * 
 * @author Alex Cash
 * @version 1.3 April 2015
 */

public class Audio {
	/** Reference to the group to which the audio tracks will be added. */
	private Group group;
	
	/** Media player object to control playback. */
	MediaPlayer player;
	
	/* UI Control Objects */
	private Button playPauseButton;
	private Button muteButton;
	private Button controlsButton;
	private Slider volumeSlider;
	private Slider progressSlider;
	private VolumeListener volumeListener;
	private SeekListener seekListener;
	private Label elapsedLabel;
	private Label remainingLabel;
	
    /* ImageViews for various GUI Elements */
    private ImageView playImage;
    private ImageView pauseImage;
    private ImageView stopImage;
    private ImageView volumeImage;
    private ImageView volumeOffImage;
    private ImageView controlsImage;
	
	/* VBoxes and HBoxes to organise the layout */
	private VBox mediaControlsVBox; 
	private VBox timeProgressVbox;
	private VBox elapsedLabelVBox;
	private VBox remainingLabelVBox;
	private HBox playSeekHbox;
	private HBox volumeMuteHBox;
	private HBox timeLabelsHBox;
	
	/* Variables */
	/** Double variable used to store the volume level so as to restore it after un-muting. */
	private double oldVolume;
	/** Double variable used to calculate the duration of the audio track. */
	private double durationMillis;
	/** Duration variable used to store the duration of the audio track. */
	private Duration duration;	
	/** Boolean variable used to indicate whether the audio was paused using the "Pause" button. */
	private boolean pausedByButton;
	/** Integer variable used to store the duration rounded to the nearest second. */
	private int roundedDuration;
	/** Integer variable used to store the duration of the audio track in hours. */
	private int durationHours;
	/** Integer variable used to store the duration of the audio track in minutes. */
	private int durationMins;
	/** Integer variable used to store the duration of the audio track in seconds. */
	private int durationSecs;
	private int durationSeconds;
	private int roundedCurrentTime;
	private int currentTimeMins;
	private int currentTimeSecs;
	/** Integer variable used to define the width of buttons used for the audio player. */
	private int buttonWidth = 30;
	/** Integer variable used to define the height of buttons used for the audio player. */
	private int buttonHeight = 30;

	/**
     * Constructs the audio track.
     * 
     * @param nGroup The group this audio track goes on.
     * 
     * @param x The x coordinate of the top left of the audio player 
     *            relative to the group's origin.
     *            
     * @param y The y coordinate of the top left of the audio player 
     *            relative to the group's origin.
     *            
     * @param width The width of the audio player in pixels.
     * 
     * @param sourcefile Absolute path of the audio track as a string. Can be a local
     *                     file path or a web address beginning with 'http'
     * 
     * @param autoPlay If true the video plays immediately
     * 
     * @param loop If true the audio track loops to the beginning when it ends
     * 
     * @param visibleControls If true, there are visible buttons displayed. If this is not true, 
     * 						autoPlay is set to true
     * 
     * @param playButtonOnly If true, the only visible button is a play/pause button. If false and 
     * 						visibleControls is true, all control buttons are visible
     * 
     */
	public Audio(Group nGroup, double x, double y, double width, String sourceFile, boolean autoPlay, boolean loop, boolean visibleControls, boolean playButtonOnly){
		/* Set the group reference */
		this.group = nGroup;

        /* Load icon images */
        playImage = new ImageView(new Image(getClass().getResourceAsStream("Play_ST_CONTENT_RECT_Transparent_L-01.png")));
        pauseImage = new ImageView(new Image(getClass().getResourceAsStream("Pause_ST_CONTENT_RECT_Transparent_L-01.png")));        
        stopImage = new ImageView(new Image(getClass().getResourceAsStream("Stop_ST_CONTENT_RECT_Transparent_L-01.png")));
        volumeImage = new ImageView(new Image(getClass().getResourceAsStream("VolumeON_ST_CONTENT_RECT_Transparent_L-01.png")));
        volumeOffImage = new ImageView(new Image(getClass().getResourceAsStream("VolumeOFF_ST_CONTENT_RECT_Transparent_L-01.png")));
        controlsImage = new ImageView(new Image(getClass().getResourceAsStream("Cog_ST_CONTENT_RECT_Transparent_L-01.png")));
        
        
        /* Create and arrange the VBoxes and HBoxes required to lay out the audio player */
		mediaControlsVBox = new VBox();
		mediaControlsVBox.setAlignment(Pos.CENTER);
		mediaControlsVBox.setSpacing(0);
		mediaControlsVBox.setMinHeight(100);
		mediaControlsVBox.relocate(x, y);

		
		playSeekHbox = new HBox();
		playSeekHbox.setAlignment(Pos.CENTER);
		playSeekHbox.setSpacing(0);
		playSeekHbox.setMinHeight(45); //To account for the size of the buttons and the slider, and their respective positions in their boxes

		
		volumeMuteHBox = new HBox();
		volumeMuteHBox.setAlignment(Pos.BOTTOM_LEFT);
		volumeMuteHBox.setSpacing(0);
		volumeMuteHBox.setMinHeight(30);

		
		timeProgressVbox = new VBox();
		timeProgressVbox.setAlignment(Pos.BOTTOM_CENTER);
		timeProgressVbox.setSpacing(0);

		
		timeLabelsHBox = new HBox();
		timeLabelsHBox.setAlignment(Pos.CENTER);
		timeLabelsHBox.setSpacing(0);
		timeLabelsHBox.setMaxWidth(width - buttonWidth - 15);

		
		elapsedLabelVBox = new VBox();
		elapsedLabelVBox.setAlignment(Pos.BASELINE_LEFT);
		elapsedLabelVBox.setSpacing(10);


		remainingLabelVBox = new VBox();
		remainingLabelVBox.setAlignment(Pos.BASELINE_RIGHT);
		remainingLabelVBox.setSpacing(10);				
		
		mediaControlsVBox.getChildren().addAll(playSeekHbox, volumeMuteHBox);
		
		
		/* playPause button with default text */
		playPauseButton = new Button ("");
		playPauseButton.setOnAction(new playHandler());
		playPauseButton.getStylesheets().add(this.getClass().getResource("MediaHandlerStyle.css").toExternalForm());
		playPauseButton.setGraphic(playImage);
		playPauseButton.setId("play");
		
		
		/* Mute button with default text */
		muteButton = new Button("");
		muteButton.setOnAction(new muteHandler());
		muteButton.getStylesheets().add(this.getClass().getResource("MediaHandlerStyle.css").toExternalForm());
		muteButton.setGraphic(volumeOffImage);
		muteButton.setId("mute");
		
		
		/* Controls button with default text */
		controlsButton = new Button("");
		controlsButton.setOnAction(new controlsHandler());		
		controlsButton.getStylesheets().add(this.getClass().getResource("MediaHandlerStyle.css").toExternalForm());
		controlsButton.setGraphic(controlsImage);
		controlsButton.setId("controls");
		
		
		/* Create new listener for the volume value */
		volumeListener = new VolumeListener();
		
		/* Define the volume slider */
		volumeSlider = new Slider();
		volumeSlider.getStylesheets().add(this.getClass().getResource("MediaHandlerStyle.css").toExternalForm());
        volumeSlider.setId("slider");
		volumeSlider.setMin(0);
		volumeSlider.setMax(100);
		volumeSlider.setValue(100);
		volumeSlider.setMajorTickUnit(10);
		volumeSlider.setBlockIncrement(5);
		volumeSlider.setShowTickLabels(false);
		volumeSlider.setShowTickMarks(false);
		volumeSlider.setSnapToTicks(true);
		volumeSlider.setOrientation(Orientation.HORIZONTAL);		
		volumeSlider.valueProperty().addListener(volumeListener);
		/* Set the size and location of the volume slider */
		volumeSlider.setPrefSize(width - (2 * buttonWidth), 5);
		
		
		/* Create new listener for the progress bar/slider */ 
		seekListener = new SeekListener();
		/* Create the slider for the progress bar */
		progressSlider = new Slider();
		progressSlider.getStylesheets().add(this.getClass().getResource("MediaHandlerStyle.css").toExternalForm());
		progressSlider.setId("slider");
		progressSlider.setMin(0);
		progressSlider.setMax(100);
		progressSlider.setValue(0);
		progressSlider.setBlockIncrement(5);
		progressSlider.setShowTickLabels(false);
		progressSlider.setShowTickMarks(false);
		progressSlider.setSnapToTicks(false);
		progressSlider.setOrientation(Orientation.HORIZONTAL);
		progressSlider.valueProperty().addListener(seekListener);
		progressSlider.setOnMousePressed(new AudioSeekHandler());
		progressSlider.setOnMouseReleased(new AudioSeekHandler());	
		/* Set the size and location of the progress slider */
		progressSlider.setPrefSize(width-buttonWidth+5, 5);
		
		
		/* Labels for tracking elapsed and remaining time */
		elapsedLabel = new Label("00:00");
		elapsedLabel.setTextFill(Color.LIGHTGRAY);
		remainingLabel = new Label("00:00");
		remainingLabel.setTextFill(Color.LIGHTGRAY);


		
		/* Create a temporary media object */
		//Media audio = new Media(sourceFile.toURI().toString());
		Media audio;
		
		/* Create a temporary file object */
		File file;
		
		/* Sourcefile handling */
		/* First check that the sourcefile is not null */
		if(sourceFile == null) {
			return;
		}
		
		/* Check whether the media file is a local or web resource */
        if(sourceFile.startsWith("http")) {
            /* File is a web resource. Check that it exists */
            if(!mediaExists(sourceFile)) {
                /* Add a label to notify the user that the media was unavailable */
                Label label = new Label("Media Unavailable");
                label.relocate(x, y);
                group.getChildren().add(label);
                
                /* Return to halt the creation of this video */
                return;
            }
            
            /* Load the media from URL */
            audio = new Media(sourceFile);
            
        } else {
            /* File is a local resource */
            file = new File(sourceFile);
            
            /* Check that the file exists and is .mp3, .aac, or .wav */
            if(!file.exists()) {
                /* Add a label to notify the user that the file could not be found */
                Label label = new Label("File could not be found");
                label.relocate(x, y);
                group.getChildren().add(label);
                return;
            } else if(!file.getAbsolutePath().endsWith(".mp3") &&
                      !file.getAbsolutePath().endsWith(".aac") &&
                      !file.getAbsolutePath().endsWith(".wav")) {
                /* Return to halt the creation of this audio */
                return;
            }

            /* Load the file as a media object */
            audio = new Media(file.toURI().toString());
        }
		

		/* Create the MediaPlayer using the chosen file */
		player = new MediaPlayer(audio);

		/* Set its autoPlay value to true if there are no buttons */
		if (visibleControls == false) {
			player.setAutoPlay(true);
		} else {
			player.setAutoPlay(autoPlay);
		}
		
		/* If the loop variable is true, set the player to cycle through the audio indefinitely*/
		if(loop) {
            player.setCycleCount(MediaPlayer.INDEFINITE);
        }

		/* Create the MediaView using the "player" MediaPlayer */
		MediaView audioView = new MediaView(player);

		/* Set the play/pause button text based on autoPlay value */
		if (player.isAutoPlay() == true) {
			playPauseButton.setGraphic(pauseImage);
		} else {
			playPauseButton.setGraphic(playImage);
		}


		/* Once the player is ready, get the information about the duration of the audio file */
		/* Use this to set the max value of the progress slider */
		player.setOnReady(new Runnable() {
			@Override
			public void run() {
				duration = audio.getDuration();
				durationMillis = duration.toMillis();
				roundedDuration = (int)Math.round(duration.toSeconds());
				durationMins = roundedDuration /60;
				durationSecs = roundedDuration - (60*durationMins);
				
				/* Set the sizes of the label VBoxes once the other boxes are initialised */
				double timeLabelSize = (timeLabelsHBox.getWidth()/2);
				elapsedLabelVBox.setMinWidth(timeLabelSize);
				remainingLabelVBox.setMinWidth(timeLabelSize);
				
				elapsedLabel.setText("00:00");
				remainingLabel.setText(String.format("-%02d:%02d",durationMins, durationSecs));
			}
		});


		player.currentTimeProperty().addListener(new progressUpdater());

		group.getChildren().addAll(mediaControlsVBox);
		
		/* Add relevant parts to the group depending on bool switches in call */		
		if (visibleControls == true && playButtonOnly == false){	//Add all visible controls				
			elapsedLabelVBox.getChildren().addAll(elapsedLabel);
			remainingLabelVBox.getChildren().addAll(remainingLabel);	
			timeLabelsHBox.getChildren().addAll(elapsedLabelVBox, remainingLabelVBox);
			timeProgressVbox.getChildren().addAll(timeLabelsHBox, progressSlider);
			playSeekHbox.getChildren().addAll(playPauseButton, timeProgressVbox);
			volumeMuteHBox.getChildren().addAll(controlsButton, volumeSlider, muteButton);
			group.getChildren().addAll(audioView);
		} else if (visibleControls == true && playButtonOnly == true) {	//Add just the play/pause button
			playSeekHbox.getChildren().addAll(playPauseButton);
			group.getChildren().addAll(audioView);
		} else {
			group.getChildren().addAll(audioView); //Add no visible controls
		}

	}
	
	/** 
     * Utility function, checks if an online media source exists
     * 
     * @param url The URL to check
     */
    private static boolean mediaExists(String url){
        try {
          /* Do not follow redirects */
          HttpURLConnection.setFollowRedirects(false);
          
          /* Open a connection to the media */
          HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
          
          /* Use a head only request to just retrieve metadata */
          con.setRequestMethod("HEAD");
          
          /* If a response code of 200 (okay) is received, the media is available */
          return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        }
        catch (Exception e) {
           return false;
        }
    }
    
	
	/** Programmatically play this audio track. */
    public void play() {
        player.play();
    }
    
    /** Programmatically pause this audio track. */
    public void pause() {
    	player.pause();
    }
    
    /** Programatically stop this audio track. */
    public void stop() {
    	player.stop();
    }
    
    /** Programmatically dispose of this audio track. */
    public void dispose() {
    	player.dispose();
    }
	
	public class progressUpdater implements ChangeListener<Duration> {		
		@Override
        public void changed(ObservableValue<? extends Duration> arg0,
                            Duration arg1, Duration arg2) {			
			double currentTime = player.getCurrentTime().toSeconds();
			int elapsedTimeSecs = (int) Math.round(currentTime);
			int elapsedTimeMins = elapsedTimeSecs / 60;
			int remainingSeconds = ((int) duration.toSeconds()- elapsedTimeSecs);
			int remainingMins = remainingSeconds / 60;
					
			double perCent = (currentTime / duration.toSeconds());
			
			if (!seekListener.isEnabled()) {
				progressSlider.setValue(perCent * 100.0);
				
				elapsedLabel.setText(String.format("%02d:%02d",elapsedTimeMins, elapsedTimeSecs - elapsedTimeMins*60));
				remainingLabel.setText(String.format("-%02d:%02d",remainingMins, remainingSeconds - remainingMins*60));
			}
        }		
	}
		
	public class AudioSeekHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent e) {
			if(e.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
				/* Enable the progress bar to be used for scanning */				
				player.pause();
				seekListener.setEnabled(true);
			}else if (e.getEventType().equals(MouseEvent.MOUSE_RELEASED)) {
				/* Disable the use of progress bar for scanning*/
				if (pausedByButton == false) {
					player.play();
				}
				seekListener.setEnabled(false);		
			}
		}
	}
	public class playHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			if (player.getStatus().equals(Status.PLAYING)) {
				player.pause();
				playPauseButton.setGraphic(playImage);
				pausedByButton = true;
			} else {
				player.play();
				playPauseButton.setGraphic(pauseImage);
				pausedByButton = false;
			}
		}
	}

	public class muteHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			if (muteButton.getGraphic().equals(volumeOffImage)) {				
				oldVolume = volumeSlider.getValue();				
				volumeSlider.setValue(0);
				player.setMute(true);
				muteButton.setGraphic(volumeImage);
			} else {
				volumeSlider.setValue(oldVolume);
				player.setMute(false);
				muteButton.setGraphic(volumeOffImage);
			}
		}
	}
	
	public class controlsHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			if (volumeMuteHBox.getChildren().contains(volumeSlider)) {
				timeProgressVbox.getChildren().remove(progressSlider);
				volumeMuteHBox.getChildren().remove(volumeSlider);
				volumeMuteHBox.getChildren().remove(muteButton);
				elapsedLabelVBox.getChildren().remove(elapsedLabel);
				remainingLabelVBox.getChildren().remove(remainingLabel);
			} else {			
				elapsedLabelVBox.getChildren().add(elapsedLabel);
				remainingLabelVBox.getChildren().add(remainingLabel);
				timeProgressVbox.getChildren().add(progressSlider);
				volumeMuteHBox.getChildren().add(volumeSlider);
				volumeMuteHBox.getChildren().add(muteButton);

			}
		}
	}
	
	public class VolumeListener implements ChangeListener<Number>{
		@Override 
		public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
			player.setVolume(volumeSlider.getValue() / 100.0);		
		}
	}
	
	public class SeekListener implements ChangeListener<Number>{
		private boolean enable;
		
		public void setEnabled(boolean isEnabled) {
			enable = isEnabled;
		}
		
		public boolean isEnabled() {
			return enable;
		}
		
		@Override 
		public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
			if (enable) {
				audioSeek(new_val.doubleValue());
				
			}
		}
	}
	
	public void audioSeek(double percentSeek) {
		Double currentValuePerCent = (percentSeek / 100);
		player.seek(Duration.millis(durationMillis * currentValuePerCent));
	}


}