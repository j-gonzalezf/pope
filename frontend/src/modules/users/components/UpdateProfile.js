import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Nav from 'react-bootstrap/Nav';
import { BsPencilSquare, BsTrashFill, BsXLg } from "react-icons/bs";
import './SignUp.css';

import { useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';

const UpdateProfile = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const user = useSelector(selectors.getUser);
    const [fullName, setFullName] = useState(user.fullName);
    const [email, setEmail] = useState(user.email);
    const [phone, setPhone] = useState(user.phone);
    const [icon, setIcon] = useState(user.icon);
    const [socialLinks, setSocialLinks] = useState(user.socialLinks);
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

            dispatch(actions.updateProfile(
                {
                    id: user.id,
                    email: email.trim(),
                    fullName: fullName.trim(),
                    phone: phone,
                    role: 'TRAINER',
                    icon: icon,
                    socialLinks: socialLinks
                },
                () => navigate('/users/clients'),
                errors => setError(errors)
            ));

        } else {
            setError(null);
            form.classList.add('was-validated');
        }
    }

    const handleSelectTab = (selectedTab) => {
        setActiveTab(selectedTab);
    }

    const handleDelete = () => {

        dispatch(actions.deleteUser(user.id,
            () => {
                navigate('/');
                dispatch(actions.logout());
            },
            errors => setError(errors)
        ));

        setShowDeleteModal(false)

    }

    return (

        <div fluid className="SignUp">

            <Card className="card bg-light border-dark">

                <Card.Header as="h3" className="card-header">
                    <FormattedMessage id="project.users.myProfile" />
                    <Nav variant="pills" className="justify-content-between" activeKey={activeTab} onSelect={handleSelectTab}>
                        <Nav.Item>
                            <Nav.Link className="text-white" eventKey="profile">
                                <FormattedMessage id="project.users.myInfo" />
                            </Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link className="text-white" eventKey="update">
                                <BsPencilSquare style={{ marginRight: '10px' }} />
                                <FormattedMessage id="project.users.updateProfile" />
                            </Nav.Link>
                        </Nav.Item>
                        <Nav.Item className="text-white">
                            <Nav.Link className="bg-danger text-white" onClick={() => setShowDeleteModal(true)}>
                                <BsTrashFill style={{ marginRight: '10px' }} />
                                <FormattedMessage id="project.users.deleteAccount" />
                            </Nav.Link>
                        </Nav.Item>
                    </Nav>
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
                                placeholder="Introduzca nombre y apellidos"
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
                                disabled={activeTab === 'profile'}
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.users.emailRequired" />
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
                                placeholder={activeTab === 'profile' ? '' : "Introduzca un teléfono"}
                                value={phone}
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
                            <Form.Label data-testid="socialLinks" htmlFor="socialLinks" className="mb-3">
                                <FormattedMessage id="project.users.socialLinks" />
                            </Form.Label>
                            <Form.Control
                                type="url"
                                className="form-control"
                                id="socialLinks"
                                name="socialLinks"
                                placeholder={activeTab === 'profile' ? '' : "Introduzca una URL (p.e. https://linktr.ee/usuario)"}
                                value={socialLinks}
                                onChange={e => setSocialLinks(e.target.value)}
                                disabled={activeTab === 'profile'}
                            />
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

                <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                    <Modal.Header closeButton>
                        <Modal.Title>
                            <FormattedMessage id="project.users.deleteAccount.title" />
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <FormattedMessage id="project.users.deleteAccount.body" />
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
                            <FormattedMessage id="project.global.button.cancel" />
                        </Button>
                        <Button variant="danger" onClick={handleDelete}>
                            <FormattedMessage id="project.users.deleteAccount.button" />
                        </Button>
                    </Modal.Footer>
                </Modal>

            </Card>

        </div>

    );

}

export default UpdateProfile;
