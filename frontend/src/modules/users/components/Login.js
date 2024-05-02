import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Container from 'react-bootstrap/Container';
import Form from 'react-bootstrap/Form';
import './Login.css';

import { useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch } from 'react-redux';
import { Link, useNavigate } from 'react-router-dom';

import { Errors } from "../../common";
import * as actions from '../actions';

const Login = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState(null);
    let form;

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {

            dispatch(actions.login(
                email.trim(),
                password,
                () => navigate("/"),
                errors => setError(errors),
                () => {
                    navigate("/users/login");
                    dispatch(actions.logout());
                }
            ));

        } else {
            setError(null);
            form.classList.add("was-validated");
        }

    }

    return (

        <Container fluid className="Login">

            <Errors errors={error} onClose={() => setError(null)} />

            <Card className="card bg-light border-dark">

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
                        <Form.Group className="form-group">
                            <Form.Label data-testid="email" htmlFor="email" className="mb-3">
                                <FormattedMessage id="project.users.email" />
                            </Form.Label>
                            <Form.Control
                                type="email"
                                className="form-control"
                                placeholder="Introuzca su correo"
                                id="email"
                                value={email}
                                onChange={event => setEmail(event.target.value)}
                                autoFocus
                                required
                            />
                        </Form.Group>
                        <Form.Group className="form-group">
                            <br />
                        </Form.Group>
                        <Form.Group className="form-group">
                            <Form.Label data-testid="password" htmlFor="password" className="mb-3">
                                <FormattedMessage id="project.users.password" />
                            </Form.Label>
                            <Form.Control
                                type="password"
                                className="form-control"
                                placeholder="Introduzca su contraseña"
                                id="password"
                                value={password}
                                onChange={event => setPassword(event.target.value)}
                                required
                            />
                        </Form.Group>
                        <Form.Group className="form-group">
                            <br />
                        </Form.Group>
                        <Form.Group className="text-center">
                            <Button data-testid="submit" type="submit" className="primary" >
                                <FormattedMessage id="project.users.login" />
                            </Button>
                        </Form.Group>
                    </Form>
                </Card.Body>
                <p className="signUp-link">
                    <Link to="/users/signup">
                        <FormattedMessage id="project.users.signUp" />
                    </Link>
                </p>
            </Card>

        </Container>

    );

}

export default Login;
