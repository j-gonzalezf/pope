import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import { BsXLg } from "react-icons/bs";
import './SignUp.css';

import { useRef, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';

const SignUp = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const [fullName, setFullName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [phone, setPhone] = useState('');
    const [icon, setIcon] = useState(null);
    const [socialLinks, setSocialLinks] = useState('');
    const [error, setError] = useState(null);
    // eslint-disable-next-line
    const [passwordsDoNotMatch, setPasswordsDoNotMatch] = useState(false);

    const fileInputRef = useRef(null);

    let confirmPasswordInput;
    let form;

    function readImage(input) {

        let file = input.target.files[0];

        if (file) {
            setIcon(file)
            file = null;
        } else {
            // Resetea el valor del input de archivo al pulsar cancel
            input.target.value = "";
            setIcon(null);
        }

    }

    function clearImage() {
        setIcon(null);
        document.getElementById('icon').value = "";
        if (fileInputRef.current) {
            fileInputRef.current.value = "";
        }
    }

    const handleSubmit = event => {
        event.preventDefault();

        if (form.checkValidity()) {
            const formData = new FormData();
            const userDto = {
                email: email.trim(),
                password: password,
                fullName: fullName.trim(),
                phone: phone ? phone.trim() : null,
                role: 'TRAINER',
                socialLinks: socialLinks ? socialLinks.trim() : null
            };
            formData.append('user', new Blob([JSON.stringify(userDto)], { type: 'application/json' }));

            if (icon) {
                formData.append('file', icon);
            }

            dispatch(actions.signUp(
                formData,
                () => {
                    navigate('/users/clients');
                },
                errors => setError(errors),
                () => {
                    navigate('/users/login');
                    dispatch(actions.logout());
                }
            ));
        } else {
            setError(null);
            form.classList.add('was-validated');
            window.scrollTo(0, 0);
        }
    }

    const handleConfirmPasswordChange = value => {
        confirmPasswordInput.setCustomValidity('');
        setConfirmPassword(value);
        setPasswordsDoNotMatch(false);
    }

    return (

        <div fluid="true" className="SignUp">

            <Card className="card signUp">

                <Card.Header as="h3" className="card-header">
                    <FormattedMessage id="project.users.signUp" />
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
                                <span className='required'>*</span>
                            </Form.Label>
                            <Form.Control
                                type="text"
                                className="form-control"
                                id="fullName"
                                name="fullName"
                                placeholder="Introduzca nombre y apellidos"
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
                                <span className='required'>*</span>
                            </Form.Label>
                            <Form.Control
                                type="email"
                                className="form-control"
                                id="email"
                                name="email"
                                placeholder="Introduzca un correo"
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
                                <span className='required'>*</span>
                            </Form.Label>
                            <Form.Control
                                type="password"
                                className="form-control"
                                id="password"
                                name="password"
                                placeholder="Introduzca una contraseña"
                                value={password}
                                onChange={e => setPassword(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.passwordRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="confirmPassword" htmlFor="confirmPassword" className="mb-3">
                                <FormattedMessage id="project.users.confirmPassword" />
                                <span className='required'>*</span>
                            </Form.Label>
                            <Form.Control
                                ref={node => confirmPasswordInput = node}
                                type="password"
                                className="form-control"
                                id="confirmPassword"
                                name="confirmPassword"
                                placeholder="Repite la contraseña"
                                value={confirmPassword}
                                onChange={e => handleConfirmPasswordChange(e.target.value)}
                                required
                                pattern={password}
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.confirmPasswordPattern" />
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
                                placeholder="Introduzca un teléfono"
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
                            <Form.Label data-testid="socialLinks" htmlFor="socialLinks" className="mb-3">
                                <FormattedMessage id="project.users.socialLinks" />
                            </Form.Label>
                            <Form.Control
                                type="url"
                                className="form-control"
                                id="socialLinks"
                                name="socialLinks"
                                placeholder="Introduzca una URL (p.e. https://linktr.ee/usuario)"
                                value={socialLinks}
                                onChange={e => setSocialLinks(e.target.value)}
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.socialLinksPattern" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3 required">
                            <Form.Label data-testid="requiredFields" className='required text'>
                                <FormattedMessage id="project.common.requiredFields" />
                                <span className='required'>*</span>
                            </Form.Label>
                        </Form.Group>

                        <Errors errors={error} onClose={() => setError(null)} />

                        <Form.Group className="text-center">
                            <Button data-testid="submit" type="submit" className="primary" >
                                <FormattedMessage id="project.users.signUp.button" />
                            </Button>
                        </Form.Group>
                    </Form>
                </Card.Body>

            </Card>

        </div>

    );

}

export default SignUp;
