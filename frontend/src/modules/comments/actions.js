import backend from '../../backend';
import * as actionTypes from "./actionTypes";

const addCommentComplete = () => ({
    type: actionTypes.ADD_COMMENT_COMPLETE
})

export const addComment = (comment, onSuccess, onErrors) => dispatch =>
    backend.commentService.addComment(comment,
        () => {
            dispatch(addCommentComplete());
            onSuccess();
        },
        onErrors
    );
