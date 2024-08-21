import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import './AddComment.css';

import { useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as userSelectors from '../../users/selectors';

const AddComment = () => {

    const dispatch = useDispatch();

    const { templateId } = useParams();

    const user = useSelector(userSelectors.getUser);

    const [comment, setComment] = useState('');
    const [error, setError] = useState(null);

    let form;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {
            dispatch(actions.addComment(
                {
                    text: comment.trim(),
                    commentDate: new Date().toISOString().slice(0, -1),
                    templateId: templateId,
                    userId: user.id
                },
                () => {
                    setComment('');
                },
                errors => setError(errors)
            ));

        } else {
            setError(null);
        }
    }

    return (
        <div className="AddComment">

            <Form
                ref={node => form = node}
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
