import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import { BsXLg } from "react-icons/bs";
import './SignUp.css';

import { useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';

const UpdateClient = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const user = useSelector(selectors.getUser);
    const [fullName, setFullName] = useState(user.fullName);
    const [email, setEmail] = useState(user.email);
    const [password, setPassword] = useState(user.password);
    const [phone, setPhone] = useState(user.phone);
    const [icon, setIcon] = useState(user.icon);
    const [birthdate, setBirthdate] = useState(user.birthdate);
    const [injuries, setInjuries] = useState(user.injuries);
    const [goals, setGoals] = useState(user.goals);
    const [height, setHeight] = useState(user.height);
    const [error, setError] = useState(null);

    let form;

    function readImage(input) {

        let file = input.target.files[0];

        if (file) {
            const fileReader = new FileReader()

            fileReader.readAsDataURL(file)
            fileReader.addEventListener("load", function () {
                let base64DataIndex = fileReader.result.indexOf(',') + 1;
                let base64Data = fileReader.result.substring(base64DataIndex);
                const newIcon = {
                    name: file.name,
                    base64: base64Data
                }
                setIcon(newIcon)
            })

        } else {
            // Resetea el valor del input de archivo al pulsar cancel
            input.target.value = "";
            setIcon(null);
        }

    }

    function clearImage() {
        setIcon(null);
        document.getElementById('icon').value = "";
    }

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {

            dispatch(actions.updateProfile(
                {
                    id: user.id,
                    email: email.trim(),
                    fullName: fullName.trim(),
                    phone: phone,
                    role: 'CLIENT',
                    icon: icon,
                    birthdate: birthdate,
                    injuries: injuries,
                    goals: goals,
                    height: height
                },
                () => navigate('/users/clients'),
                errors => setError(errors)
            ));

        } else {
            setError(null);
            form.classList.add('was-validated');
        }
    }

    return (

        <div fluid className="SignUp">

            <Card className="card bg-light border-dark">

                <Card.Header as="h3" className="card-header">
                    <FormattedMessage id="project.users.updateProfile" />
                </Card.Header>

                <Card.Body className="card-body">
                    <Form
                        ref={node => form = node}
                        className="needs-validation"
                        noValidate
                        onSubmit={e => handleSubmit(e)}
                    >
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="fullName" htmlFor="fullName" className="mb-3">
                                <FormattedMessage id="project.users.fullName" />
                            </Form.Label>
                            <Form.Control
                                type="text"
                                className="form-control"
                                id="fullName"
                                name="fullName"
                                placeholder="Introduzca nombre y apellidos del cliente"
                                value={fullName}
                                onChange={e => setFullName(e.target.value)}
                                required
                                autoFocus
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.fullNameRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="email" htmlFor="email" className="mb-3">
                                <FormattedMessage id="project.users.email" />
                            </Form.Label>
                            <Form.Control
                                type="email"
                                className="form-control"
                                id="email"
                                name="email"
                                placeholder="Introduzca el correo del cliente"
                                value={email}
                                onChange={e => setEmail(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.emailRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="password" htmlFor="password" className="mb-3">
                                <FormattedMessage id="project.users.password" />
                            </Form.Label>
                            <Form.Control
                                type="password"
                                className="form-control"
                                id="password"
                                name="password"
                                placeholder="Introduzca una clave de acceso para el cliente"
                                value={password}
                                onChange={e => setPassword(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.passwordRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="phone" htmlFor="phone" className="mb-3">
                                <FormattedMessage id="project.users.phone" />
                            </Form.Label>
                            <Form.Control
                                type="text"
                                className="form-control"
                                id="phone"
                                name="phone"
                                placeholder="Introduzca el teléfono del cliente"
                                value={phone}
                                onChange={e => setPhone(e.target.value)}
                                pattern='[0-9]*'
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.phonePattern" />
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="icon" htmlFor="icon" className="mb-3">
                                <FormattedMessage id="project.users.icon" />
                            </Form.Label>
                            <div style={{ display: 'flex', alignItems: 'center' }}>
                                <Form.Control
                                    type="file"
                                    className="form-control"
                                    id="icon"
                                    name="icon"
                                    accept="image/png, image/jpg, image/jpeg"
                                    onChange={readImage}
                                />
                                {icon && (
                                    <Button variant="danger" onClick={clearImage} style={{ marginLeft: '10px' }}>
                                        <BsXLg />
                                    </Button>
                                )}
                            </div>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="birthdate" htmlFor="birthdate" className="mb-3">
                                <FormattedMessage id="project.users.birthdate" />
                            </Form.Label>
                            <Form.Control
                                type="date"
                                className="form-control"
                                id="birthdate"
                                name="birthdate"
                                value={birthdate}
                                onChange={e => setBirthdate(e.target.value)}
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.birthdatePattern" />
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="injuries" htmlFor="injuries" className="mb-3">
                                <FormattedMessage id="project.users.injuries" />
                            </Form.Label>
                            <Form.Control
                                as="textarea" rows={3}
                                className="form-control"
                                id="injuries"
                                name="injuries"
                                placeholder="Introduzca los impedimentos y/o lesiones del cliente"
                                value={injuries}
                                onChange={e => setInjuries(e.target.value)}
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="goals" htmlFor="goals" className="mb-3">
                                <FormattedMessage id="project.users.goals" />
                            </Form.Label>
                            <Form.Control
                                as="textarea" rows={3}
                                className="form-control"
                                id="goals"
                                name="goals"
                                placeholder="Introduzca los objetivos a corto y/o largo plazo del cliente"
                                value={goals}
                                onChange={e => setGoals(e.target.value)}
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="height" htmlFor="height" className="mb-3">
                                <FormattedMessage id="project.users.height" />
                            </Form.Label>
                            <Form.Control
                                type="number"
                                className="form-control"
                                id="height"
                                name="height"
                                placeholder="Introduzca la altura del cliente (en cm)"
                                value={height}
                                onChange={e => setHeight(e.target.value)}
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.heightPattern" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Errors errors={error} onClose={() => setError(null)} />

                        <Form.Group className="text-center">
                            <Button data-testid="submit" type="submit" className="primary" >
                                <FormattedMessage id="project.users.updateProfile.button" />
                            </Button>
                        </Form.Group>
                    </Form>
                </Card.Body>

            </Card>

        </div>

    );

}

export default UpdateClient;
