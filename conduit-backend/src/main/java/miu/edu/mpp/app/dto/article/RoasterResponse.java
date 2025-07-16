package miu.edu.mpp.app.dto.article;

import java.util.List;

public class RoasterResponse {
    private List<RoasterUserArticle> roaster;

    public RoasterResponse(List<RoasterUserArticle> roaster) {
        this.roaster = roaster;
    }

    public List<RoasterUserArticle> getRoaster() {
        return roaster;
    }

    public void setRoaster(List<RoasterUserArticle> roaster) {
        this.roaster = roaster;
    }
}
