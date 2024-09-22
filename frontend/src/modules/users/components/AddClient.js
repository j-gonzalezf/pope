import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import { BsXLg } from "react-icons/bs";
import './AddClient.css';

import { useRef, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';

const AddClient = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const [fullName, setFullName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [phone, setPhone] = useState('');
    const [icon, setIcon] = useState(null);
    const [birthdate, setBirthdate] = useState(null);
    const [injuries, setInjuries] = useState('');
    const [goals, setGoals] = useState('');
    const [height, setHeight] = useState('');
    const [weight, setWeight] = useState('');
    const [error, setError] = useState(null);

    const fileInputRef = useRef(null);

    const user = useSelector(selectors.getUser);
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
            const heightValue = height === '0' ? null : height;
            const weightValue = weight === '0' ? null : weight;

            const userDto = {
                email: email.trim(),
                password: password,
                fullName: fullName.trim(),
                phone: phone ? phone.trim() : null,
                role: 'CLIENT',
                birthdate: birthdate || null,
                injuries: injuries ? injuries.trim() : null,
                goals: goals ? goals.trim() : null,
                height: heightValue || null,
                weight: weightValue || null,
                trainerId: user.id
            };

            const formData = new FormData();
            formData.append('user', new Blob([JSON.stringify(userDto)], { type: 'application/json' }));

            if (icon) {
                formData.append('file', icon);
            }

            dispatch(actions.addClient(
                formData,
                () => {
                    navigate('/users/clients');
                },
                errors => setError(errors)
            ));
        } else {
            setError(null);
            form.classList.add('was-validated');
            window.scrollTo(0, 0);
        }
    }

    return (

        <div fluid="true" className="AddClient">

            <Card className="card addClient">

                <Card.Header as="h3" className="card-header">
                    <FormattedMessage id="project.users.addClient.title" />
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
                                <span className='required'>*</span>
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
                                <FormattedMessage id="project.users.passwordClient" />
                                <span className='required'>*</span>
                            </Form.Label>
                            <Form.Control
                                type="password"
                                className="form-control"
                                id="password"
                                name="password"
                                placeholder="Introduzca una contraseña para el cliente"
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
                                value={birthdate || ''}
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
                                value={height || ''}
                                onChange={e => setHeight(e.target.value)}
                                min={0}
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.heightPattern" />
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="weight" htmlFor="weight" className="mb-3">
                                <FormattedMessage id="project.users.weight" />
                            </Form.Label>
                            <Form.Control
                                type="number"
                                step="0.01"
                                className="form-control weight"
                                id="weight"
                                name="weight"
                                placeholder="Introduzca el peso del cliente (en kg)"
                                value={weight || ''}
                                onChange={e => setWeight(e.target.value)}
                                min={0}
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.weightPattern" />
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
                                <FormattedMessage id="project.users.addClient.button" />
                            </Button>
                        </Form.Group>
                    </Form>
                </Card.Body>

            </Card>

        </div>

    );

}

export default AddClient;
