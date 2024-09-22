import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import './ChangePassword.css';

import { useEffect, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';

const ChangePassword = () => {

    const user = useSelector(selectors.getUser);
    const dispatch = useDispatch();
    const navigate = useNavigate();

    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmNewPassword, setConfirmNewPassword] = useState('');
    const [error, setError] = useState(null);
    // eslint-disable-next-line
    const [passwordsDoNotMatch, setPasswordsDoNotMatch] = useState(false);

    let form;
    let confirmPasswordInput;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {
            form.classList.remove('was-validated');
            form.classList.add('was-validated');

            dispatch(actions.changePassword(user.id, oldPassword, newPassword,
                () => navigate('/users/clients'),
                errors => setError(errors)));

        } else {
            setError(null);
            form.classList.add('was-validated');
        }
    }

    const handleConfirmPasswordChange = value => {
        confirmPasswordInput.setCustomValidity('');
        setConfirmNewPassword(value);
        setPasswordsDoNotMatch(false);
    }

    useEffect(() => {
        return () => {
            setOldPassword('');
            setNewPassword('');
            setConfirmNewPassword('');
            setError(null);
            setPasswordsDoNotMatch(false);
        };
    }, []);

    return (

        <div fluid="true" className="ChangePassword">

            <Card className="card cp">

                <Card.Header as="h3" className="card-header">
                    <FormattedMessage id="project.users.changePassword" />
                </Card.Header>

                <Card.Body className="card-body">
                    <Form
                        ref={node => form = node}
                        className="needs-validation"
                        noValidate
                        onSubmit={e => handleSubmit(e)}
                    >
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="oldPassword" htmlFor="oldPassword" className="mb-3">
                                <FormattedMessage id="project.users.oldPassword" />
                            </Form.Label>
                            <Form.Control
                                type="password"
                                className="form-control"
                                id="oldPassword"
                                name="oldPassword"
                                placeholder="Introduzca su contraseña actual"
                                value={oldPassword}
                                onChange={e => setOldPassword(e.target.value)}
                                required
                                autoFocus
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.oldPasswordRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="newPassword" htmlFor="newPassword" className="mb-3">
                                <FormattedMessage id="project.users.newPassword" />
                            </Form.Label>
                            <Form.Control
                                type="password"
                                className="form-control"
                                id="newPassword"
                                name="newPassword"
                                placeholder="Introduzca una nueva contraseña"
                                value={newPassword}
                                onChange={e => setNewPassword(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.newPasswordRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="confirmNewPassword" htmlFor="confirmNewPassword" className="mb-3">
                                <FormattedMessage id="project.users.confirmNewPassword" />
                            </Form.Label>
                            <Form.Control
                                ref={node => confirmPasswordInput = node}
                                type="password"
                                className="form-control"
                                id="confirmNewPassword"
                                name="confirmNewPassword"
                                placeholder="Repite la nueva contraseña"
                                value={confirmNewPassword}
                                onChange={e => handleConfirmPasswordChange(e.target.value)}
                                required
                                pattern={newPassword}
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.confirmPasswordPattern" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Errors errors={error} onClose={() => setError(null)} />

                        <Form.Group className="text-center">
                            <Button data-testid="submit" type="submit" className="primary" >
                                <FormattedMessage id="project.users.changePassword.button" />
                            </Button>
                        </Form.Group>
                    </Form>
                </Card.Body>

            </Card>

        </div>

    );

}

export default ChangePassword;
