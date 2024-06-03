import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Table from 'react-bootstrap/Table';
import { BsBoxArrowInRight, BsFillPlusCircleFill, BsPencilSquare } from "react-icons/bs";
import './CyclesList.css';

import { useEffect, useState } from 'react';
import { FormattedMessage } from 'react-intl';
import { useDispatch, useSelector } from 'react-redux';
import { Link, useNavigate, useParams } from 'react-router-dom';

import { Errors } from '../../common';
import * as actions from '../actions';
import * as selectors from '../selectors';
import * as userSelectors from '../../users/selectors';

const CyclesList = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

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

    let formCreate;
    let formUpdate;

    function dateFormat(date) {
        let d = new Date(date);
        let month = '' + (d.getMonth() + 1);
        let day = '' + d.getDate();
        let year = d.getFullYear().toString().slice(-2);

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;
        return [day, month, year].join('/');
    }

    // Para poder agrandar la fila seleccionada (scale) al pasar el cursor sin que desborde la tabla
    function addEventListenersToRows() {
        // Encuentra todos los elementos con la clase 'cycleSelect'
        const rows = document.querySelectorAll('.cycleSelect');
        // Añade un 'event listener' para cada fila para el evento 'mouseenter'
        rows.forEach(row => {
            row.addEventListener('mouseenter', () => {
                // Cuando el cursor está sobre la fila, aplica 'overflow-x: hidden' al contenedor
                document.querySelector('.table-responsive').style.overflowX = 'hidden';
            });
            row.addEventListener('mouseleave', () => {
                // Cuando el cursor sale de la fila, remueve el estilo 'overflow-x'
                document.querySelector('.table-responsive').style.overflowX = '';
            });
        });
    }

    const handleSubmit = event => {

        event.preventDefault();

        if (formCreate.checkValidity()) {

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
                        () => {
                            setTimeout(addEventListenersToRows, 0);
                        },
                        errors => setError(errors)));
                },
                errors => setError(errors)
            ));

        } else {
            setError(null);
            formCreate.classList.add('was-validated');
        }
    }

    const handleUpdateCycle = event => {

        event.preventDefault();

        if (formUpdate.checkValidity()) {

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
            formUpdate.classList.add('was-validated');
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

    const closeModal = () => {
        setShowAddCycleModal(false);
        setShowUpdateCycleModal(false);
        setId(null);
        setName('');
        setDescription(null);
        setFromDate(null);
        setToDate(null)
    }

    const redirectToCycleDetails = cycle => {
        navigate(`/templates/trainingCycles/${clientId}/trainingCycle/${cycle.id}`);
    }

    useEffect(() => {

        dispatch(actions.getCycles(user.id, clientId,
            () => {
                setTimeout(addEventListenersToRows, 0);
            },
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

                <Modal.Body className='modal-body cycle'>
                    <Form
                        ref={node => formCreate = node}
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
                    <Button variant="secondary" onClick={closeModal}>
                        <FormattedMessage id="project.global.button.cancel" />
                    </Button>
                    <Button className="primary cycle" onClick={handleSubmit}>
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

                <Modal.Body className='modal-body cycle'>
                    <Form
                        ref={node => formUpdate = node}
                        className="needs-validation"
                        noValidate
                        onSubmit={e => handleUpdateCycle(e)}
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
                    <Button variant="secondary" onClick={closeModal}>
                        <FormattedMessage id="project.global.button.cancel" />
                    </Button>
                    <Button className="primary cycle" onClick={handleUpdateCycle}>
                        <FormattedMessage id="project.templates.updateCycle" />
                    </Button>
                </Modal.Footer>

            </Modal>

            <div class="table-responsive">
                <Table className="table">
                    <thead>
                        <tr>
                            <th className="customTable"></th>
                            <th className="customTable underline"><FormattedMessage id="project.templates.cycleName" /></th>
                            <th className="customTable underline"><FormattedMessage id="project.templates.cycleDescription" /></th>
                            <th className="customTable underline"><FormattedMessage id="project.templates.cycleFrom" /></th>
                            <th className="customTable underline"><FormattedMessage id="project.templates.cycleTo" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        {getCycles.map((cycle) => (
                            <tr key={cycle.id} className="cycleSelect" onClick={() => redirectToCycleDetails(cycle)}>
                                <td className="pointer"><BsBoxArrowInRight size={20} /></td>
                                <td className="customTable cycleName">{cycle.name}</td>
                                <td className="customTable">{cycle.description}</td>
                                <td className="customTable">{dateFormat(cycle.fromDate)}</td>
                                <td className="customTable">{dateFormat(cycle.toDate)}</td>
                                <td className="customTable">
                                    <Link className='link' onClick={() => openUpdateCycleModal(cycle)}
                                        title='Pulsa para editar ciclo'>
                                        <BsPencilSquare className="editIconStyle cycle" />
                                        <span>
                                            <FormattedMessage id="project.templates.updateCycle" />
                                        </span>
                                    </Link>
                                </td>
                            </tr>
                        ))}
                        <tr>
                            <td className="customTable"></td>
                            <td colSpan={4} className="customTable">
                                <Button className="primary cycle" onClick={() => setShowAddCycleModal(true)}>
                                    <BsFillPlusCircleFill className="plusIconStyle cycle" />
                                    <span>
                                        <b><FormattedMessage id="project.templates.addCycle" /></b>
                                    </span>
                                </Button>
                            </td>
                        </tr>
                    </tbody>
                </Table>
            </div>

            <Errors errors={error} onClose={() => setError(null)} />

        </div>

    );

}

export default CyclesList;
