package app;

import jiro.java.util.MyProperties;

import app.config.ConfigStage;
import app.layout.PositionsFlowPane;
import app.menubar.MyMenuBar;
import app.standard.Standards;
import util.AlertUtils;

import java.io.*;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController {

  /** 画像やアニメの規格 */
  private Standards walkStandard;
  private Standards sideViewStandard;

  private ConfigStage cs;

  private Optional<ConfigStage> configStageOpt = Optional.empty();

  // FXMLコンポーネント//{{{

  @FXML private MyMenuBar myMenuBar;
  @FXML private PositionsFlowPane positionsFlowPane;

  //}}}

  // 初期化

  @FXML private void initialize() {//{{{

    myMenuBar.setMainController(this);

  }//}}}

  // メソッド

  public void drawImage(File file) {//{{{

    cs = new ConfigStage(positionsFlowPane, this);

    MyProperties mp = new MyProperties("./presets/walk/mv.preset");
    mp.load();

    final String DEF_W = "48";

    // TODO TEST VALUE
    int width     = Integer . parseInt(mp . getProperty("chara.width")  . orElse(DEF_W));
    int height    = Integer . parseInt(mp . getProperty("chara.height") . orElse(DEF_W));
    int row       = Integer . parseInt(mp . getProperty("row")          . orElse("4"));
    int column    = Integer . parseInt(mp . getProperty("column")       . orElse("3"));

    walkStandard = new Standards.Builder(width, height)
      .row(row) .column(column)
      .build();

    String filePath = file.getPath();
    positionsFlowPane.drawWalkImage(filePath, walkStandard);

    myMenuBar.setDisableConfigMenuItem(false);

  }//}}}

  public void drawSideViewImage(File file) {//{{{

    cs = new ConfigStage(positionsFlowPane, this);

    MyProperties mp = new MyProperties(file.getPath());
    mp.load();

    //// TODO TEST VALUE
    int row        = Integer . parseInt(mp . getProperty("row")          . orElse("6"));
    int column     = Integer . parseInt(mp . getProperty("column")       . orElse("9"));
    int frameCount = Integer . parseInt(mp . getProperty("frameCount")   . orElse("3"));

    // サイドビューは歩行グラと規格が異なるため
    sideViewStandard = new Standards.Builder(1, 1)
      .row(row) .column(column) .frameCount(frameCount)
      .build();

    String filePath = file.getPath();
    positionsFlowPane.drawSideViewImage(filePath, sideViewStandard);

    myMenuBar.setDisableConfigMenuItem(false);

  }//}}}

  public void showConfigStage() {//{{{

    configStageOpt = Optional.ofNullable(cs);
    configStageOpt.ifPresent(c -> {
      if (c.isShowing())
        c.hide();
      else
        c.show();
    });

  }//}}}

  void resizeConfigStage() {//{{{

    configStageOpt
      .ifPresent(c -> {
        c.resize(positionsFlowPane);
      });

  }//}}}

  public void updateZoomRate(double zoom) {//{{{

    positionsFlowPane.updateZoomRate(zoom);

  }//}}}

  public void updateAnimationSpeed(int duration) {//{{{

    positionsFlowPane.updateAnimationSpeed(duration);

  }//}}}

  public void changeAlwaysOnTop() {//{{{

    Stage stage = getStage();
    boolean alwaysOnTop = getStage().isAlwaysOnTop();
    stage.setAlwaysOnTop(!alwaysOnTop);

  }//}}}

  public void showLanguageAlert() {//{{{
    AlertUtils.showLanguageAlert();
  }//}}}

  private Stage getStage() {//{{{

    return (Stage) positionsFlowPane.getScene().getWindow();

  }//}}}

  // Setter

  public void setFontSize(String fontSize) {//{{{

    VBox root = (VBox) positionsFlowPane.getScene().lookup("#root");
    root.setStyle("-fx-font-size:" + fontSize + "pt;");

  }//}}}

}
