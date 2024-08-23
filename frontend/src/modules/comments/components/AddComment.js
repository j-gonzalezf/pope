import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import './AddComment.css';

import { useEffect, useRef, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';
import * as userSelectors from '../../users/selectors';

const AddComment = () => {

    const dispatch = useDispatch();

    const { templateId } = useParams();

    const user = useSelector(userSelectors.getUser);
    const comments = useSelector(selectors.getComments);

    const [comment, setComment] = useState('');
    const [error, setError] = useState(null);

    const commentBoxRef = useRef(null);

    let commentForm;

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        date.setHours(date.getHours() + 2);
        const hours = date.getHours().toString().padStart(2, '0');
        const minutes = date.getMinutes().toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const year = date.getFullYear().toString().slice(-2);
        return `${hours}:${minutes} - ${day}/${month}/${year}`;
    }


    const handleSubmit = event => {

        event.preventDefault();

        if (commentForm.checkValidity()) {
            dispatch(actions.addComment(
                {
                    text: comment.trim(),
                    commentDate: new Date().toISOString().slice(0, -1),
                    templateId: templateId,
                    userId: user.id
                },
                () => {
                    setComment('');
                    dispatch(actions.getTemplateComments(templateId,
                        () => { },
                        errors => setError(errors)
                    ));
                },
                errors => setError(errors)
            ));

        } else {
            setError(null);
        }
    }

    useEffect(() => {
        dispatch(actions.getTemplateComments(templateId,
            () => { },
            errors => setError(errors)
        ));
    }, [dispatch, templateId]);

    useEffect(() => {
        if (commentBoxRef.current) {
            commentBoxRef.current.scrollTop = commentBoxRef.current.scrollHeight;
        }
    }, [comments]);

    return (
        <div className="AddComment">

            <div className="comment-box" ref={commentBoxRef}>
                {comments.map((comment) => (
                    <div key={comment.id}
                        className={`comment ${comment.userId === user.id ? 'user-comment' : 'other-comment'}`}>
                        <p>{comment.text}</p>
                        <small>{formatDate(comment.commentDate)}</small>
                    </div>
                ))}
            </div>

            <Form
                ref={node => commentForm = node}
                noValidate
                onSubmit={e => handleSubmit(e)}
            >
                <div className="btn-group comment" role="group" aria-label="Comment Bar">
                    <Form.Group className="mb-3 comment">
                        <Form.Control
                            type="text"
                            className="comment-input"
                            id="comment"
                            name="comment"
                            placeholder="Añade un comentario"
                            value={comment}
                            onChange={e => setComment(e.target.value)}
                            required
                        />
                    </Form.Group>
                    <Button className="primary comment" type="submit">
                        <FormattedMessage id="project.comments.add" />
                    </Button>
                </div>
            </Form>

            <Errors errors={error} onClose={() => setError(null)} />

        </div>
    );
}

export default AddComment;
