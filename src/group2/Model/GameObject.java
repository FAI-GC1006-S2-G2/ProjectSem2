package group2.Model;

import group2.Geometric.Rect;

/**
 * Created by BTC on 2015/12/17.
 */

public abstract class GameObject extends Sprite {

   public Rect collisionBoundingBox() {
      return new Rect(position.x - this.size.width / 2, position.y - this.size.height / 2, this.size.width, this.size.height);
   }

   public GameObject(String imageNamed) {
      super(imageNamed);
   }

   public GameObject(){}
}

