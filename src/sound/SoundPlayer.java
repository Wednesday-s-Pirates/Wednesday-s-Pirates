package sound;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**wavファイルを再生するためのクラス
 *※ゲーム内の音源に関する操作は本クラスから行って下さい。*/

public class SoundPlayer {
	private static Clip bgmClip;

	public Clip getBgmClip() {
		return bgmClip;
	}
	
	/*ファイルを取得し、再生する準備を行うメソッド*/
	public static Clip createClip(File path) {
		AudioInputStream ais = null;
		try {
			ais = AudioSystem.getAudioInputStream(path);
			//ファイルの形式取得
			AudioFormat format = ais.getFormat();

			//単一のオーディオ形式を含む指定した情報からデータラインの情報オブジェクトを構築
			DataLine.Info info = new DataLine.Info(Clip.class, format);

			//指定されたLine.Infoオブジェクトの記述に一致するラインを取得
			Clip newClip = (Clip) AudioSystem.getLine(info);

			//再生準備完了
			newClip.open(ais);

			return newClip;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ais != null)
					ais.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void playBGMFromResource(String resourcePath) {
		new Thread(() -> {
			try {
				// 先に再生中のBGMがあれば止める
				if (bgmClip != null && bgmClip.isRunning()) {
					bgmClip.stop();
					bgmClip.flush();
					bgmClip.close();
					bgmClip = null;
				}

				// 新しいBGMの読み込み
				URL url = SoundPlayer.class.getClassLoader().getResource(resourcePath);
				if (url == null) {
					System.err.println("BGMリソースが見つかりません: " + resourcePath);
					return;
				}

				try (AudioInputStream ais = AudioSystem.getAudioInputStream(url)) {
					AudioFormat format = ais.getFormat();
					DataLine.Info info = new DataLine.Info(Clip.class, format);
					Clip newClip = (Clip) AudioSystem.getLine(info);
					newClip.open(ais);
					newClip.loop(Clip.LOOP_CONTINUOUSLY);
					bgmClip = newClip;
				}
			} catch (Exception e) {
				System.err.println("BGMリソースの再生に失敗しました: " + resourcePath);
				e.printStackTrace();
			}
		}).start();
	}

	
	//resourcesフォルダからSEを再生するメソッド※resourcesフォルダはビルドパスの追加を行ってください！
	//resourccesフォルダ右クリック→ビルドパス→ソースフォルダとして使用から追加できます
	
	public static void playSEFromResource(String resourcePath) {
		new Thread(() -> {
			try (AudioInputStream ais = AudioSystem.getAudioInputStream(
					//jar化した際にリソースを取得する動作
					SoundPlayer.class.getClassLoader().getResource(resourcePath))) {

				AudioFormat format = ais.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				Clip seClip = (Clip) AudioSystem.getLine(info);
				seClip.open(ais);
				seClip.start();

			} catch (Exception e) {
				System.err.println("効果音リソースの再生に失敗しました: " + resourcePath);
				e.printStackTrace();
			}
		}).start();
	}
}


/*6.19変更点
 *99行目、playSEをstaticに変更
 *17行目、フィールドbgmClipをstaticに変更、代入可能なBGMを一つのみにする
 */
/*6.23変更点
 * 133~新規メソッドの追加、従来のplayBGM,playSEと動作は同じだがjarファイルにした際でも動作するようなメソッドに変更
 * 64~132行目、上記実装によりコメントアウト化
 */

