package mert.kadakal.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NasilGidilirFragment extends Fragment {

    private ImageView imageView;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private ScaleGestureDetector scaleGestureDetector;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private float minZoom = 0.44f;
    private float maxZoom = 2f;
    private float currentZoom = 0.44f;
    private boolean basildi_mi = false;
    private String baslangıc = null;
    private String hedef = null;
    HashMap<String, ArrayList<String>> komsular = new HashMap<>();
    HashMap<String, HashMap<String, Integer>> komsular_mesafe = new HashMap<>();
    ArrayList<String> tem = new ArrayList<>();
    HashMap<String, Integer> mesafeMap = new HashMap<>();
    HashMap<ArrayList<String>, Integer> paths_and_lenghts = new HashMap<>();
    HashMap<String, ArrayList<Integer>> node_konumlari = new HashMap<>();
    HashMap<String, ArrayList<Integer>> node_konumlari_shortestpath = new HashMap<>();
    private Button sifirla;

    private CustomImageView customImageView;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nasil_gidilir, container, false);

        imageView = view.findViewById(R.id.imageView);
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());

        customImageView = view.findViewById(R.id.imageViewDraw);
        customImageView.setVisibility(view.INVISIBLE);

        sifirla = view.findViewById(R.id.sifirla);
        sifirla.setVisibility(view.INVISIBLE);
        sifirla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customImageView.setVisibility(view.INVISIBLE);
                customImageView.clearLines();
                imageView.setVisibility(view.VISIBLE);
                sifirla.setVisibility(view.INVISIBLE);
                imageView.post(() -> fitImageToScreen());
            }
        });

        //node ve komşuları tanımlama
        ArrayList<Integer> konumlar;
        //1
        tem.add("2");
        tem.add("6");
        mesafeMap.put("2", 4);
        mesafeMap.put("6", 8);
        komsular.put("1", new ArrayList<>(tem));
        komsular_mesafe.put("1", new HashMap<>(mesafeMap));
        tem.clear();
        mesafeMap.clear();
        konumlar = new ArrayList<>(Arrays.asList(211, 1130));
        node_konumlari_shortestpath.put("1", konumlar);
        //2
        tem.add("1");
        tem.add("3");
        mesafeMap.put("1", 4);
        mesafeMap.put("3", 1);
        komsular.put("2", new ArrayList<>(tem));
        komsular_mesafe.put("2", new HashMap<>(mesafeMap));
        tem.clear();
        mesafeMap.clear();
        konumlar = new ArrayList<>(Arrays.asList(260, 1126));
        node_konumlari_shortestpath.put("2", konumlar);
        //3
        tem.add("2");
        tem.add("4");
        mesafeMap.put("2", 1);
        mesafeMap.put("4", 2);
        komsular.put("3", new ArrayList<>(tem));
        komsular_mesafe.put("3", new HashMap<>(mesafeMap));
        tem.clear();
        mesafeMap.clear();
        konumlar = new ArrayList<>(Arrays.asList(278, 1146));
        node_konumlari_shortestpath.put("3", konumlar);
        //4
        tem.add("3");
        tem.add("5");
        tem.add("7");
        mesafeMap.put("3", 2);
        mesafeMap.put("5", 3);
        mesafeMap.put("7", 2);
        komsular.put("4", new ArrayList<>(tem));
        komsular_mesafe.put("4", new HashMap<>(mesafeMap));
        tem.clear();
        mesafeMap.clear();
        konumlar = new ArrayList<>(Arrays.asList(278, 1188));
        node_konumlari_shortestpath.put("4", konumlar);
        //5
        tem.add("4");
        tem.add("10");
        mesafeMap.put("4", 3);
        mesafeMap.put("10", 5);
        komsular.put("5", new ArrayList<>(tem));
        komsular_mesafe.put("5", new HashMap<>(mesafeMap));
        tem.clear();
        mesafeMap.clear();
        konumlar = new ArrayList<>(Arrays.asList(329, 1181));
        node_konumlari_shortestpath.put("5", konumlar);
        //6
        tem.add("1");
        tem.add("7");
        tem.add("8");
        mesafeMap.put("1", 8);
        mesafeMap.put("7", 3);
        mesafeMap.put("8", 3);
        komsular.put("6", new ArrayList<>(tem));
        komsular_mesafe.put("6", new HashMap<>(mesafeMap));
        tem.clear();
        mesafeMap.clear();
        konumlar = new ArrayList<>(Arrays.asList(211, 1220));
        node_konumlari_shortestpath.put("6", konumlar);
        //7
        tem.add("4");
        tem.add("6");
        tem.add("9");
        mesafeMap.put("4", 2);
        mesafeMap.put("6", 3);
        mesafeMap.put("9", 3);
        komsular.put("7", new ArrayList<>(tem));
        komsular_mesafe.put("7", new HashMap<>(mesafeMap));
        tem.clear();
        mesafeMap.clear();
        konumlar = new ArrayList<>(Arrays.asList(286, 1220));
        node_konumlari_shortestpath.put("7", konumlar);
        //8
        tem.add("6");
        tem.add("9");
        mesafeMap.put("6", 3);
        mesafeMap.put("9", 3);
        komsular.put("8", new ArrayList<>(tem));
        komsular_mesafe.put("8", new HashMap<>(mesafeMap));
        tem.clear();
        mesafeMap.clear();
        konumlar = new ArrayList<>(Arrays.asList(211, 1270));
        node_konumlari_shortestpath.put("8", konumlar);
        //9
        tem.add("7");
        tem.add("8");
        tem.add("10");
        mesafeMap.put("7", 3);
        mesafeMap.put("8", 3);
        mesafeMap.put("10", 3);
        komsular.put("9", new ArrayList<>(tem));
        komsular_mesafe.put("9", new HashMap<>(mesafeMap));
        tem.clear();
        mesafeMap.clear();
        konumlar = new ArrayList<>(Arrays.asList(287, 1270));
        node_konumlari_shortestpath.put("9", konumlar);
        //10
        tem.add("5");
        tem.add("9");
        mesafeMap.put("5", 5);
        mesafeMap.put("9", 3);
        komsular.put("10", new ArrayList<>(tem));
        komsular_mesafe.put("10", new HashMap<>(mesafeMap));
        tem.clear();
        mesafeMap.clear();
        konumlar = new ArrayList<>(Arrays.asList(329, 1270));
        node_konumlari_shortestpath.put("10", konumlar);

        imageView.post(() -> fitImageToScreen());

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        mode = DRAG;
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            matrix.set(savedMatrix);
                            matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                            fixTranslation();  // Ekrandan taşmayı engelle
                        } else if (mode == ZOOM) {
                            float newDist = spacing(event);
                            if (newDist > 10f) {
                                matrix.set(savedMatrix);
                                float scale = newDist / oldDist;
                                currentZoom = getMatrixScale();
                                float desiredZoom = scale * currentZoom;

                                // Zoom sınırlarını kontrol et
                                if (desiredZoom < minZoom) {
                                    scale = minZoom / currentZoom;
                                } else if (desiredZoom > maxZoom) {
                                    scale = maxZoom / currentZoom;
                                }

                                matrix.postScale(scale, scale, mid.x, mid.y);
                                fixTranslation();  // Ekrandan taşmayı engelle
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                }

                imageView.setImageMatrix(matrix);

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = event.getX();
                    float y = event.getY();

                    // Tıklanan noktayı görselin koordinatlarına dönüştür
                    PointF imagePoint = getImagePointFromTouch(x, y);
                    // imagePoint.x ve imagePoint.y, görselin koordinat sistemindeki noktayı verir

                    //seçim komtrol edilir ve uygunsa en kısa yol hesaplanır //imagePoint.x imagePoint.y
                    if (!(basildi_mi)) {
                        String secilen_node = secilen_node_belirle(imagePoint.x, imagePoint.y);
                        if (!(secilen_node.equals("none"))) {
                            basildi_mi = true;
                            baslangıc = secilen_node;
                            Log.d("node", "ilk node seçildi: " + baslangıc);
                        } else {
                            Log.d("node", "lütfen node seçiniz");
                        }
                    } else {
                        String secilen_node = secilen_node_belirle(imagePoint.x, imagePoint.y);
                        if (!(secilen_node.equals("none"))) {
                            basildi_mi = false;

                            hedef = secilen_node;
                            Log.d("node", "hedef node seçildi: " + secilen_node);

                            // Başlangıç yolu
                            ArrayList<String> path = new ArrayList<>();
                            path.add(baslangıc);

                            // En kısa yolları bulma
                            paths_and_lenghts.clear();
                            shortest(path, baslangıc, 0);
                            Log.d("tag", paths_and_lenghts.toString());

                            ArrayList<String> minPath = new ArrayList<>();
                            int minValue = Integer.MAX_VALUE;
                            for (ArrayList<String> key : paths_and_lenghts.keySet()) {
                                if (paths_and_lenghts.get(key) < minValue) {
                                    minValue = paths_and_lenghts.get(key);
                                    minPath = key;
                                }
                            }

                            customImageView.setVisibility(view.VISIBLE);
                            imageView.setVisibility(view.INVISIBLE);
                            sifirla.setVisibility(view.VISIBLE);

                            for (int i = 0; i < minPath.size() - 1; i++) {
                                float x1 = node_konumlari_shortestpath.get(minPath.get(i)).get(0);
                                float y1 = node_konumlari_shortestpath.get(minPath.get(i)).get(1);
                                float x2 = node_konumlari_shortestpath.get(minPath.get(i+1)).get(0);
                                float y2 = node_konumlari_shortestpath.get(minPath.get(i+1)).get(1);
                                customImageView.setLineCoordinates(x1, y1, x2, y2);
                                customImageView.draw(new Canvas());
                            }
                        }
                    }
                }

                return true;
            }
        });

        return view;
    }

    public void shortest(ArrayList<String> path, String last_node, int lenght) {
        Log.d("tag", String.valueOf(path.size()));

        // Hedefe ulaşma durumunu kontrol et
        if (path.get(path.size() - 1).equals(hedef)) {
            paths_and_lenghts.put(path, lenght);
            return;
        }

        // Komşuları kontrol et
        ArrayList<String> neighbors = komsular.get(last_node);
        if (neighbors != null) {
            for (String node : neighbors) {
                // Daha önce ziyaret edilmemiş düğümleri kontrol et
                if (!(path.contains(node))) {
                    ArrayList<String> tem = new ArrayList<>(path);
                    tem.add(node);
                    shortest(tem, node, lenght + komsular_mesafe.get(last_node).get(node)); // Mesafeyi ekliyoruz
                }
            }
        }
    }

    private String secilen_node_belirle(float x, float y) {
        if (Math.sqrt(Math.pow(Math.abs(469 - x), 2) + Math.pow(Math.abs(1572 - y), 2)) < 10) {
            return "1";
        }
        if (Math.sqrt(Math.pow(Math.abs(583 - x), 2) + Math.pow(Math.abs(1566 - y), 2)) < 10) {
            return "2";
        }
        if (Math.sqrt(Math.pow(Math.abs(611 - x), 2) + Math.pow(Math.abs(1596 - y), 2)) < 10) {
            return "3";
        }
        if (Math.sqrt(Math.pow(Math.abs(611 - x), 2) + Math.pow(Math.abs(1685 - y), 2)) < 10) {
            return "4";
        }
        if (Math.sqrt(Math.pow(Math.abs(724 - x), 2) + Math.pow(Math.abs(1685 - y), 2)) < 10) {
            return "5";
        }
        if (Math.sqrt(Math.pow(Math.abs(469 - x), 2) + Math.pow(Math.abs(1755 - y), 2)) < 10) {
            return "6";
        }
        if (Math.sqrt(Math.pow(Math.abs(613 - x), 2) + Math.pow(Math.abs(1755 - y), 2)) < 10) {
            return "7";
        }
        if (Math.sqrt(Math.pow(Math.abs(469 - x), 2) + Math.pow(Math.abs(1866 - y), 2)) < 10) {
            return "8";
        }
        if (Math.sqrt(Math.pow(Math.abs(613 - x), 2) + Math.pow(Math.abs(1866 - y), 2)) < 10) {
            return "9";
        }
        if (Math.sqrt(Math.pow(Math.abs(724 - x), 2) + Math.pow(Math.abs(1186 - y), 2)) < 10) {
            return "10";
        }
        return "none";
    }

    // Görseli ekrana sığdırma işlemi
    private void fitImageToScreen() {
        Drawable drawable = imageView.getDrawable();
        if (drawable == null) {
            return;
        }

        int imageWidth = drawable.getIntrinsicWidth();
        int imageHeight = drawable.getIntrinsicHeight();

        int viewWidth = imageView.getWidth();
        int viewHeight = imageView.getHeight();

        float scale = Math.min((float) viewWidth / imageWidth, (float) viewHeight / imageHeight);

        float dx = (viewWidth - imageWidth * scale) / 2;
        float dy = (viewHeight - imageHeight * scale) / 2;

        matrix.setScale(scale, scale);
        matrix.postTranslate(dx, dy);

        imageView.setImageMatrix(matrix);
    }

    // Ekrandan taşmayı engellemek için translation düzeltmesi
    private void fixTranslation() {
        RectF rect = getMatrixRect();
        float width = rect.width();
        float height = rect.height();

        float deltaX = 0, deltaY = 0;

        if (width > imageView.getWidth()) {
            if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < imageView.getWidth()) {
                deltaX = imageView.getWidth() - rect.right;
            }
        } else {
            deltaX = (imageView.getWidth() - width) / 2 - rect.left;
        }

        if (height > imageView.getHeight()) {
            if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < imageView.getHeight()) {
                deltaY = imageView.getHeight() - rect.bottom;
            }
        } else {
            deltaY = (imageView.getHeight() - height) / 2 - rect.top;
        }

        matrix.postTranslate(deltaX, deltaY);
    }

    // Görselin ekrandaki pozisyonunu ve boyutlarını al
    private RectF getMatrixRect() {
        Drawable drawable = imageView.getDrawable();
        if (drawable == null) {
            return new RectF();
        }

        RectF rect = new RectF(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        matrix.mapRect(rect);
        return rect;
    }

    // İki parmak arasındaki mesafeyi hesapla
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    // İki parmak arasındaki orta noktayı hesapla
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    // Matrisin şu anki zoom değerini al
    private float getMatrixScale() {
        float[] values = new float[9];
        matrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    // Ekran koordinatlarını görsel koordinatlarına dönüştür
    private PointF getImagePointFromTouch(float x, float y) {
        Matrix inverseMatrix = new Matrix();
        matrix.invert(inverseMatrix);

        float[] point = new float[]{x, y};
        inverseMatrix.mapPoints(point);

        return new PointF(point[0], point[1]);
    }

    // Pinch-to-zoom işlemi için ScaleGestureDetector kullanımı
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            currentZoom = getMatrixScale();
            float desiredZoom = scaleFactor * currentZoom;

            if (desiredZoom < minZoom) {
                scaleFactor = minZoom / currentZoom;
            } else if (desiredZoom > maxZoom) {
                scaleFactor = maxZoom / currentZoom;
            }

            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            fixTranslation();  // Ekrandan taşmayı engelle
            imageView.setImageMatrix(matrix);
            return true;
        }
    }
}