import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HighScore {
    List<PlayerScore> scoreList = new ArrayList<>();

    public void load() throws IOException {
        String name;
        int score;
        BufferedReader br = new BufferedReader(new FileReader(""));
        try {
            while (br.ready()) { //Kollar om man kan läsa samma line utan att hoppa ett steg
                String line = br.readLine();
                if(line!=null) {
                    String[] delar = line.split(", ");
                    name = delar[0];
                    score = Integer.parseInt(delar[1]);
                    PlayerScore pScore = new PlayerScore(name, score);
                    scoreList.add(pScore);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String yourName){
        String skriv;
        int count = 0;
        int count2 = 0;
        try {
            load();
            File file = new File("");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            for(PlayerScore p: scoreList){
                if(p.getName().equals(yourName)){
                    int plus = p.getScore()+1;
                    skriv = p.getName() + ", " + plus;
                }else {
                    skriv = p.getName() + ", " + p.getScore();
                }
                bw.write(skriv);
                if(scoreList.size() > count ) {
                    bw.write("\n");
                    count++;
                }
                if(!p.getName().equals(yourName)){
                    count2++;
                }
            }
            if(count2 >= scoreList.size()){
                bw.write(yourName + ", " + 1);
            }
            bw.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void sort(){
        try {
            load();
        }catch (IOException e){
            e.printStackTrace();
        }
        PlayerScore[]array = new PlayerScore[scoreList.size()];
        for (int x = 0; x<scoreList.size();x++){
            array[x] = scoreList.get(x);
        }
        boolean swapped = true;
        int j = 0;
        PlayerScore tmp;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < array.length - j; i++) {
                if (array[i].getScore() > array[i + 1].getScore()) {
                    tmp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = tmp;
                    swapped = true;
                }
            }
        }
        scoreList.clear();
        for (PlayerScore p: array){
            scoreList.add(p);
        }
    }
}
