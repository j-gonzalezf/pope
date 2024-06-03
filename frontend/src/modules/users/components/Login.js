import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import './Login.css';

import { useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch } from 'react-redux';
import { Link, useNavigate, useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';

const Login = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const { userType } = useParams();

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    let form;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {

            dispatch(actions.login(
                email.trim(),
                password,
                () => navigate(userType === 'client' ? '../../app/components/NotFoundPage' : '/users/clients'),
                errors => setError(errors),
                () => {
                    navigate('/users/login');
                    dispatch(actions.logout());
                }
            ));

        } else {
            setError(null);
            form.classList.add('was-validated');
        }

    }

    return (

        <div fluid className="Login">

            <Card className="card login">

                <Card.Header as="h3" className="card-header">
                    <FormattedMessage id="project.users.login" />
                </Card.Header>

                <Card.Body className="card-body">
                    <Form
                        ref={node => form = node}
                        className="needs-validation"
                        noValidate
                        onSubmit={handleSubmit}
                    >
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="email" htmlFor="email" className="mb-3">
                                <FormattedMessage id="project.users.email" />
                            </Form.Label>
                            <Form.Control
                                type="email"
                                className="form-control"
                                id="email"
                                name="email"
                                placeholder="Introuzca su correo"
                                value={email}
                                onChange={event => setEmail(event.target.value)}
                                required
                                autoFocus
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.emailRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        {userType === 'client' ? (
                            <Form.Group className="mb-3">
                                <Form.Label data-testid="password" htmlFor="password" className="mb-3">
                                    <FormattedMessage id="project.users.passwordClient" />
                                </Form.Label>
                                <Form.Control
                                    type="password"
                                    className="form-control"
                                    id="password"
                                    name="password"
                                    placeholder="Introduzca la clave de acceso que le ha enviado su entrenador"
                                    value={password}
                                    onChange={event => setPassword(event.target.value)}
                                    required
                                />
                                <Form.Control.Feedback type="invalid">
                                    <FormattedMessage id="project.users.passwordClientRequired" />
                                </Form.Control.Feedback>
                            </Form.Group>)
                            :
                            (<Form.Group className="mb-3">
                                <Form.Label data-testid="password" htmlFor="password" className="mb-3">
                                    <FormattedMessage id="project.users.password" />
                                </Form.Label>
                                <Form.Control
                                    type="password"
                                    className="form-control"
                                    id="password"
                                    name="password"
                                    placeholder="Introduzca su contraseña"
                                    value={password}
                                    onChange={event => setPassword(event.target.value)}
                                    required
                                />
                                <Form.Control.Feedback type="invalid">
                                    <FormattedMessage id="project.users.passwordRequired" />
                                </Form.Control.Feedback>
                            </Form.Group>
                            )}

                        <Errors errors={error} onClose={() => setError(null)} />

                        <Form.Group className="text-center">
                            <Button type="submit" data-testid="submit" className="primary" >
                                <FormattedMessage id="project.users.login.button" />
                            </Button>
                        </Form.Group>
                    </Form>
                </Card.Body>

                {userType === 'trainer' && (

                    <p className="signUp-question">
                        <FormattedMessage id="project.users.signUp.question" />
                        <br />
                        <Link className="link" to="/users/signup">
                            <FormattedMessage id="project.users.signUp" />
                        </Link>
                    </p>

                )}

            </Card>

        </div>

    );

}

export default Login;
