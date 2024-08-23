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

const getTemplateCommentsComplete = getComments => ({
    type: actionTypes.GET_TEMPLATE_COMMENTS_COMPLETE,
    getComments
})

export const getTemplateComments = (id, onSuccess, onErrors) => dispatch =>
    backend.commentService.getTemplateComments(id,
        getComments => {
            dispatch(getTemplateCommentsComplete(getComments));
            onSuccess();
        },
        onErrors
    );
