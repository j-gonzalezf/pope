import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Row from 'react-bootstrap/Row';
import { BsFillPlusCircleFill, BsPencilSquare } from "react-icons/bs";
import './CyclesList.css';

import { useEffect, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';
import * as userSelectors from '../../users/selectors';

const CyclesList = () => {

    const dispatch = useDispatch();
    //const navigate = useNavigate();

    const user = useSelector(userSelectors.getUser);
    const { clientId } = useParams();
    const getCycles = useSelector(selectors.getCycles);

    const [id, setId] = useState(null);
    const [name, setName] = useState('');
    const [description, setDescription] = useState(null);
    const [fromDate, setFromDate] = useState(null);
    const [toDate, setToDate] = useState(null);
    const [error, setError] = useState(null);
    const [showAddCycleModal, setShowAddCycleModal] = useState(false);
    const [showUpdateCycleModal, setShowUpdateCycleModal] = useState(false);

    let form;

    function dateFormat(date) {
        let d = new Date(date);
        let month = '' + (d.getMonth() + 1);
        let day = '' + d.getDate();
        let year = d.getFullYear().toString().slice(-2);

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;
        return [day, month, year].join('/');
    }

    const handleSubmit = event => {

        event.preventDefault();

        if (form.checkValidity()) {

            dispatch(actions.createCycle(
                {
                    name: name.trim(),
                    description: description,
                    fromDate: fromDate,
                    toDate: toDate,
                    trainerId: user.id,
                    clientId: clientId
                },
                () => {
                    setShowAddCycleModal(false);
                    setName('');
                    setDescription(null);
                    setFromDate(null);
                    setToDate(null)
                    dispatch(actions.getCycles(user.id, clientId,
                        () => { },
                        errors => setError(errors)));
                },
                errors => setError(errors)
            ));

        } else {
            setError(null);
            form.classList.add('was-validated');
        }
    }

    const handleUpdateCycle = event => {

        event.preventDefault();

        if (form.checkValidity) {
            
            dispatch(actions.updateCycle(
                {
                    id: id,
                    name: name.trim(),
                    description: description,
                    fromDate: fromDate,
                    toDate: toDate,
                    trainerId: user.id,
                    clientId: clientId
                },
                () => {
                    setShowUpdateCycleModal(false);
                    setName('');
                    setDescription(null);
                    setFromDate(null);
                    setToDate(null)
                    dispatch(actions.getCycles(user.id, clientId,
                        () => { },
                        errors => setError(errors)));
                },
                errors => setError(errors)
            ));

        } else {
            setError(null);
            form.classList.add('was-validated');
        }

    }

    const openUpdateCycleModal = cycle => {
        setId(cycle.id);
        setName(cycle.name);
        setDescription(cycle.description);
        setFromDate(cycle.fromDate);
        setToDate(cycle.toDate);
        setShowUpdateCycleModal(true);
    }

    useEffect(() => {

        dispatch(actions.getCycles(user.id, clientId,
            () => { },
            errors => setError(errors)));

    }, [dispatch, user.id, clientId]);

    return (
        <div fluid className='CyclesList'>

            <Modal show={showAddCycleModal} onHide={() => setShowAddCycleModal(false)} className='modal'>
                <Modal.Header className='modal-header'>
                    <Modal.Title as="h3">
                        <FormattedMessage id="project.templates.addCycle.title" />
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form
                        ref={node => form = node}
                        className="needs-validation"
                        noValidate
                        onSubmit={e => handleSubmit(e)}
                    >
                        <Form.Group className="mb-3">

                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleName" />
                            </Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Ingrese el nombre del ciclo de entrenamiento"
                                value={name}
                                onChange={e => setName(e.target.value)}
                                required
                                autoFocus
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.cycleNameRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleDescription" />
                            </Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={3}
                                placeholder="Ingrese una descripción para el ciclo de entrenamiento"
                                value={description}
                                onChange={e => setDescription(e.target.value)}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleFromDate" />
                            </Form.Label>
                            <Form.Control
                                type="date"
                                value={fromDate}
                                onChange={e => setFromDate(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.cycleFromDateRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleToDate" />
                            </Form.Label>
                            <Form.Control
                                type="date"
                                value={toDate}
                                onChange={e => setToDate(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.cycleToDateRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                    </Form>

                    <Errors errors={error} onClose={() => setError(null)} />

                </Modal.Body>

                <Modal.Footer className='modal-footer'>
                    <Button variant="secondary" onClick={() => setShowAddCycleModal(false)}>
                        <FormattedMessage id="project.global.button.cancel" />
                    </Button>
                    <Button variant="primary" onClick={handleSubmit}>
                        <FormattedMessage id="project.templates.addCycle.button" />
                    </Button>
                </Modal.Footer>

            </Modal>

            <Modal show={showUpdateCycleModal} onHide={() => setShowUpdateCycleModal(false)} className='modal'>
                <Modal.Header className='modal-header'>
                    <Modal.Title as="h3">
                        <FormattedMessage id="project.templates.updateCycle.title" />
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form
                        ref={node => form = node}
                        className="needs-validation"
                        noValidate
                        onSubmit={e => handleSubmit(e)}
                    >
                        <Form.Group className="mb-3">
                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleName" />
                            </Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Ingrese el nombre del ciclo de entrenamiento"
                                value={name}
                                onChange={e => setName(e.target.value)}
                                required
                                autoFocus
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.cycleNameRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleDescription" />
                            </Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={3}
                                placeholder="Ingrese una descripción para el ciclo de entrenamiento"
                                value={description}
                                onChange={e => setDescription(e.target.value)}
                            />
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleFromDate" />
                            </Form.Label>
                            <Form.Control
                                type="date"
                                value={fromDate}
                                onChange={e => setFromDate(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.cycleFromDateRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>
                                <FormattedMessage id="project.templates.cycleToDate" />
                            </Form.Label>
                            <Form.Control
                                type="date"
                                value={toDate}
                                onChange={e => setToDate(e.target.value)}
                                required
                            />
                            <Form.Control.Feedback type="invalid">
                                <FormattedMessage id="project.templates.cycleToDateRequired" />
                            </Form.Control.Feedback>
                        </Form.Group>

                    </Form>

                    <Errors errors={error} onClose={() => setError(null)} />

                </Modal.Body>

                <Modal.Footer className='modal-footer'>
                    <Button variant="secondary" onClick={() => setShowUpdateCycleModal(false)}>
                        <FormattedMessage id="project.global.button.cancel" />
                    </Button>
                    <Button variant="primary" onClick={handleUpdateCycle}>
                        <FormattedMessage id="project.templates.updateCycle" />
                    </Button>
                </Modal.Footer>

            </Modal>

            <Row className="listStyle">

                {getCycles && (getCycles.map((cycle) => (

                    <Col xs={12} sm={6} md={4} lg={3} className="listItemStyle"
                        key={cycle.id}>

                        <Card className='text-center'>
                            <Card.Body>
                                <Card.Title className='text-center'>
                                    <b>{cycle.name}</b>
                                </Card.Title>
                                <Card.Text className='description'>
                                    {cycle.description}
                                </Card.Text>
                                <Card.Text>
                                    <b>{dateFormat(cycle.fromDate)} - {dateFormat(cycle.toDate)}</b>
                                </Card.Text>
                            </Card.Body>
                        </Card>
                        <br />
                        <div className='edit'>
                            <BsPencilSquare className='editIcon' onClick={() => openUpdateCycleModal(cycle)}
                                title='Pulsa para editar ciclo' />
                            <FormattedMessage id="project.templates.updateCycle" />
                        </div>
                    </Col>
                )))}

                <Col xs={12} sm={6} md={4} lg={3} className="listItemStyle" onClick={() => setShowAddCycleModal(true)}>
                    <Card className='text-center'>
                        <BsFillPlusCircleFill className='plus' size={50} color='grey' />
                        <Card.Body>
                            <Card.Title className='text-center'>
                                <b><FormattedMessage id="project.templates.addCycle" /></b>
                            </Card.Title>
                        </Card.Body>
                    </Card>
                    <br />
                </Col>

            </Row>

            <Errors errors={error} onClose={() => setError(null)} />

        </div>

    );

}

export default CyclesList;
