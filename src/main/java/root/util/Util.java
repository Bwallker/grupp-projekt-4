package root.util;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import root.model.Album;

import java.util.Optional;

public class Util {
    public static Optional<TreeItem<Album>> findNode(TreeView<Album> tree, Album target) {
        return findNode(tree.getRoot(), target);
    }

    public static Optional<TreeItem<Album>> findNode(TreeItem<Album> curr, Album target) {
        if (curr == null) return Optional.empty();
        if (curr.getValue().equals(target)) {
            return Optional.of(curr);
        }
        for (final var node : curr.getChildren()) {
            var res = (findNode(node, target));
            if (res.isPresent()) {
                return res;
            }
        }
        return Optional.empty();
    }
}
