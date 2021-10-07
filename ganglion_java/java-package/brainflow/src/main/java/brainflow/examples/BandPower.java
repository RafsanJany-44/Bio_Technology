package brainflow.examples;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.complex.Complex;

import brainflow.BoardIds;
import brainflow.BoardShim;
import brainflow.BrainFlowInputParams;
import brainflow.DataFilter;
import brainflow.DetrendOperations;
import brainflow.LogLevels;
import brainflow.WindowFunctions;
import brainflow.BoardDescr;

public class BandPower
{

    public static void main (String[] args) throws Exception
    {

        BoardShim.enable_board_logger ();
        BrainFlowInputParams params = new BrainFlowInputParams ();
        int board_id = BoardIds.GANGLION_BOARD.get_code ();
        BoardShim board_shim = new BoardShim (board_id, params);
        BoardDescr board_descr = BoardShim.get_board_descr (BoardDescr.class, board_id);
        int sampling_rate = board_descr.sampling_rate;
        int nfft = DataFilter.get_nearest_power_of_two (sampling_rate);

        board_shim.prepare_session ();
        board_shim.start_stream (3600);
        BoardShim.log_message (LogLevels.LEVEL_INFO.get_code (), "Start sleeping in the main thread");
        Thread.sleep (10000);
        board_shim.stop_stream ();
        double[][] data = board_shim.get_board_data ();
        board_shim.release_session ();

        List<Integer> eeg_channels = board_descr.eeg_channels;
        // seconds channel of synthetic board has big 'alpha' use it for test
        int eeg_channel = eeg_channels.get (1).intValue ();
        // optional: detrend before psd
        DataFilter.detrend (data[eeg_channel], DetrendOperations.LINEAR.get_code ());
        Pair<double[], double[]> psd = DataFilter.get_psd_welch (data[eeg_channel], nfft, nfft / 2, sampling_rate,
                WindowFunctions.HANNING.get_code ());
        double band_power_gamma = DataFilter.get_band_power (psd, 31.0, 40.0);
        double band_power_beta = DataFilter.get_band_power (psd, 14.0, 30.0);
        double band_power_alpha = DataFilter.get_band_power (psd, 8.0, 13.0);
        double band_power_theta = DataFilter.get_band_power (psd, 4.0, 7.0);
        double band_power_delta = DataFilter.get_band_power (psd, 1.0, 3.0);

        //System.out.println ("Alpha/Beta Ratio: " + (band_power_alpha / band_power_beta));
        System.out.println ("band_power_gamma : " + band_power_gamma );
        System.out.println ("band_power_beta : " + band_power_beta );
        System.out.println ("band_power_alpha : " + band_power_alpha );
        System.out.println ("band_power_theta : " + band_power_theta );
        System.out.println ("band_power_delta : " + band_power_delta );
    }
}
