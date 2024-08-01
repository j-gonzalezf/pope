import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Nav from 'react-bootstrap/Nav';
import { BsInfoSquare, BsPencilSquare, BsTrashFill, BsXLg } from "react-icons/bs";
import './UpdateProfile.css';

import { useEffect, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';

const UpdateClient = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const { clientId } = useParams();

    const user = useSelector(selectors.getUser);
    const client = useSelector(selectors.getClientInfo);

    const [fullName, setFullName] = useState(client.fullName);
    const [email, setEmail] = useState(client.email);
    //const [password, setPassword] = useState(client.password);
    const [phone, setPhone] = useState(client.phone);
    const [icon, setIcon] = useState(client.icon);
    const [birthdate, setBirthdate] = useState(client.birthdate);
    const [injuries, setInjuries] = useState(client.injuries);
    const [goals, setGoals] = useState(client.goals);
    const [height, setHeight] = useState(client.height);
    const [weight, setWeight] = useState(client.weight);
    const [error, setError] = useState(null);
    const [activeTab, setActiveTab] = useState('profile');
    const [showDeleteModal, setShowDeleteModal] = useState(false);

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

            const heightValue = height === '0' ? null : height;
            const weightValue = weight === '0' ? null : weight;

            dispatch(actions.updateClient(
                {
                    id: client.id,
                    email: email.trim(),
                    fullName: fullName.trim(),
                    phone: phone ? phone.trim() : null,
                    role: 'CLIENT',
                    icon: icon,
                    birthdate: birthdate || null,
                    injuries: injuries ? injuries.trim() : null,
                    goals: goals ? goals.trim() : null,
                    height: heightValue || null,
                    weight: weightValue || null,
                    trainerId: user.id
                },
                () => navigate('/users/clientDetails/' + client.id),
                errors => setError(errors)
            ));

        } else {
            setError(null);
            form.classList.add('was-validated');
            window.scrollTo(0, 0);
        }
    }

    const handleSelectTab = (selectedTab) => {

        setActiveTab(selectedTab);

        // Si se selecciona el tab 'update' o 'profile', 
        // se mueve el scroll al principio de la página
        if (selectedTab === 'update' || selectedTab === 'profile') {
            window.scrollTo(0, 0);
        }

    }

    const handleDelete = () => {

        dispatch(actions.deleteUser(client.id,
            () => {
                navigate('/users/clients');
            },
            errors => setError(errors)
        ));

        setShowDeleteModal(false)

    }

    useEffect(() => {
        dispatch(actions.getClientInfo(clientId,
            () => { },
            errors => setError(errors)));
    }, [dispatch, clientId]);

    return (

        <div fluid="true" className="UpdateProfile">

            <Card className="card updateProfile">

                <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)} className="modal">
                    <Modal.Header className="modal-header deleteProfile">
                        <Modal.Title>
                            <FormattedMessage id="project.users.deleteClient.title" />
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body className="modal-body">
                        <FormattedMessage id="project.users.deleteClient.body" />
                    </Modal.Body>
                    <Modal.Footer className="modal-footer">
                        <Button className="btn" variant="secondary" onClick={() => setShowDeleteModal(false)}>
                            <FormattedMessage id="project.global.button.cancel" />
                        </Button>
                        <Button className="btn" variant="danger" onClick={handleDelete}>
                            <FormattedMessage id="project.users.deleteAccount.button" />
                        </Button>
                    </Modal.Footer>
                </Modal>

                <Card.Header as="h3" className="card-header">
                    {activeTab === 'profile' && (
                        <FormattedMessage id="project.users.clientProfile" />
                    )}
                    {activeTab === 'update' && (
                        <FormattedMessage id="project.users.updateClient" />
                    )}
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
                                {activeTab === 'update' && (
                                    <span className='required'>*</span>
                                )}
                            </Form.Label>
                            <Form.Control
                                type="text"
                                className="form-control"
                                id="fullName"
                                name="fullName"
                                placeholder={activeTab === 'profile' ? '' : "Introduzca nombre y apellidos del cliente"}
                                value={fullName}
                                onChange={e => setFullName(e.target.value)}
                                required
                                autoFocus
                                disabled={activeTab === 'profile'}
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.fullNameRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="email" htmlFor="email" className="mb-3">
                                <FormattedMessage id="project.users.email" />
                                {activeTab === 'update' && (
                                    <span className='required'>*</span>
                                )}
                            </Form.Label>
                            <Form.Control
                                type="email"
                                className="form-control"
                                id="email"
                                name="email"
                                placeholder={activeTab === 'profile' ? '' : "Introduzca el correo del cliente"}
                                value={email}
                                onChange={e => setEmail(e.target.value)}
                                required
                                disabled={activeTab === 'profile'}
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.emailRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>
                        {/*
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
                                disabled={activeTab === 'profile'}
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.passwordRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>
                        */}
                        <Form.Group className="mb-3">
                            <Form.Label data-testid="phone" htmlFor="phone" className="mb-3">
                                <FormattedMessage id="project.users.phone" />
                            </Form.Label>
                            <Form.Control
                                type="text"
                                className="form-control"
                                id="phone"
                                name="phone"
                                placeholder={activeTab === 'profile' ? '' : "Introduzca el teléfono del cliente"}
                                value={phone || ''}
                                onChange={e => setPhone(e.target.value)}
                                pattern='[0-9]*'
                                disabled={activeTab === 'profile'}
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
                                    disabled={activeTab === 'profile'}
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
                                disabled={activeTab === 'profile'}
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
                                placeholder={activeTab === 'profile' ? '' : "Introduzca los impedimentos y/o lesiones del cliente"}
                                value={injuries || ''}
                                onChange={e => setInjuries(e.target.value)}
                                disabled={activeTab === 'profile'}
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
                                placeholder={activeTab === 'profile' ? '' : "Introduzca los objetivos a corto y/o largo plazo del cliente"}
                                value={goals || ''}
                                onChange={e => setGoals(e.target.value)}
                                disabled={activeTab === 'profile'}
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
                                placeholder={activeTab === 'profile' ? '' : "Introduzca la altura del cliente (en cm)"}
                                value={height}
                                onChange={e => setHeight(e.target.value)}
                                disabled={activeTab === 'profile'}
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
                                placeholder={activeTab === 'profile' ? '' : "Introduzca el peso	del cliente (en kg)"}
                                value={weight}
                                onChange={e => setWeight(e.target.value)}
                                disabled={activeTab === 'profile'}
                                min={0}
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.weightPattern" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Errors errors={error} onClose={() => setError(null)} />

                        {activeTab === 'update' && (
                            <Form.Group className="text-center">
                                <Button data-testid="submit" type="submit" className="primary" >
                                    <FormattedMessage id="project.users.updateProfile.button" />
                                </Button>
                            </Form.Group>
                        )}
                    </Form>
                </Card.Body>

                <Card.Footer className="card-footer">
                    <Nav variant="pills" className="justify-content-between" activeKey={activeTab} onSelect={handleSelectTab}>
                        {activeTab === 'profile' && (
                            <Nav.Item>
                                <Nav.Link className="nav-link updateProfile" eventKey="update">
                                    <BsPencilSquare style={{ marginRight: '10px' }} />
                                    <FormattedMessage id="project.users.updateClient" />
                                </Nav.Link>
                            </Nav.Item>
                        )}
                        {activeTab === 'update' && (
                            <Nav.Item>
                                <Nav.Link className="nav-link updateProfile" eventKey="profile">
                                    <BsInfoSquare style={{ marginRight: '10px' }} />
                                    <FormattedMessage id="project.users.clientProfile" />
                                </Nav.Link>
                            </Nav.Item>
                        )}
                        <Nav.Item>
                            <Nav.Link className="bg-danger nav-link deleteProfile" onClick={() => setShowDeleteModal(true)}>
                                <BsTrashFill style={{ marginRight: '10px' }} />
                                <FormattedMessage id="project.users.deleteClient" />
                            </Nav.Link>
                        </Nav.Item>
                    </Nav>
                </Card.Footer>

            </Card>

        </div>

    );

}

export default UpdateClient;
