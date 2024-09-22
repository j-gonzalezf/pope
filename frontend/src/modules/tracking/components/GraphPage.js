import { BsQuestionCircle } from "react-icons/bs";

import React, { useState } from 'react';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import { FormattedMessage } from 'react-intl';
import { useSelector } from 'react-redux';
import { useNavigate, useParams } from 'react-router-dom';
import { Chart } from 'react-charts';

import * as selectors from '../selectors';
import './GraphPage.css';

const GraphPage = () => {

    const navigate = useNavigate();

    const sensations = useSelector(selectors.getSensations);
    const weights = useSelector(selectors.getWeights);

    const { sensationKey } = useParams();
    const { clientId } = useParams();

    const [timeRange, setTimeRange] = useState(6);
    const [weekBtn, setWeekBtn] = useState(true);
    const [monthBtn, setMonthBtn] = useState(false);
    const [yearBtn, setYearBtn] = useState(false);

    const now = new Date();

    const filteredData = sensationKey !== 'weight' ?
        sensations.map(sensation => {
            const date = new Date(sensation.sensationDate);
            const formattedDate = date.toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' });
            return {
                primary: formattedDate,
                secondary: sensation[sensationKey],
                date: date
            };
        }) : weights.map(weight => {
            const date = new Date(weight.weightDate);
            const formattedDate = date.toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' });
            return {
                primary: formattedDate,
                secondary: weight.weight,
                date: date
            };
        });

    const filterByTimeRange = (data, days) => {
        const pastDate = new Date(now);
        pastDate.setDate(now.getDate() - days);
        return data.filter(item => item.date >= pastDate);
    };

    const lastData = filterByTimeRange(filteredData, timeRange).reverse();

    const chartData = React.useMemo(
        () => [
            {
                label: `${sensationKey} Data`,
                data: lastData,
            },
        ],
        [lastData, sensationKey]
    );

    const axes = React.useMemo(
        () => {
            return [
                {
                    primary: true, type: 'ordinal', position: 'bottom',
                    format: d => {
                        if (timeRange !== 7) {
                            const step = Math.ceil(lastData.length / 7);
                            const index = lastData.findIndex(data => data.primary === d);
                            return index % step === 0 ? d : '';
                        }
                        return d;
                    }
                },
                sensationKey !== 'weight' ?
                    { type: 'linear', position: 'left', hardMin: 1, hardMax: 5, format: d => (d % 1 === 0 ? Math.floor(d) : '') } :
                    { secondary: true, type: 'linear', position: 'left' }
            ];
        },
        [timeRange, lastData, sensationKey]
    );

    const getSeriesStyle = React.useCallback(
        () => ({
            stroke: '#e6af2e',
        }),
        []
    );

    const getDatumStyle = React.useCallback(
        () => ({
            r: 1,
            stroke: '#e6af2e',
            fill: '#e6af2e',
        }),
        []
    );

    const tooltip = React.useMemo(
        () => ({
            render: ({ datum }) => (
                datum ? (
                    <div className="custom-tooltip">
                        <strong>{datum.secondary}</strong>
                        <br />
                        {datum.primary}
                    </div>
                ) : null
            ),
        }),
        []
    );

    const handleTitleClick = () => {
        if (sensationKey === 'weight') {
            navigate(`/users/updateClient/${clientId}`);
        }
    };

    const renderTooltip = (props) => {
        return (
            <Tooltip id="button-tooltip" {...props}>
                {(() => {
                    switch (sensationKey) {
                        case 'fatigue':
                            return <FormattedMessage id="project.tooltips.graphsPage.fatigue" />;
                        case 'stiffness':
                            return <FormattedMessage id="project.tooltips.graphsPage.stiffness" />;
                        case 'motivation':
                            return <FormattedMessage id="project.tooltips.graphsPage.motivation" />;
                        case 'sleep':
                            return <FormattedMessage id="project.tooltips.graphsPage.sleep" />;
                        case 'weight':
                            return <FormattedMessage id="project.tooltips.graphsPage.weight" />;
                        default:
                            return null;
                    }
                })()}
            </Tooltip>
        );
    };

    return (
        <div fluid="true" className='GraphPage'>
            <h3 className={`title ${sensationKey}`} onClick={handleTitleClick}>
                {sensationKey !== 'weight' ?
                    <FormattedMessage id={`project.tracking.${sensationKey}`} />
                    : <FormattedMessage id="project.users.weight" />
                }
                <OverlayTrigger
                    placement="right"
                    delay={{ show: 200, hide: 400 }}
                    overlay={renderTooltip}
                >
                    <span className="d-inline-block" style={{ marginLeft: '10px' }}>
                        <BsQuestionCircle className="checkIconStyle" color='#e6af2e' size={20} />
                    </span>
                </OverlayTrigger>
            </h3>
            <div className='GraphContainer'>
                <div className="btn-group" role="group" aria-label="Time range">
                    <button className={weekBtn ? "btn primary graph" : "btn notSelected"} onClick={() => { setTimeRange(6); setWeekBtn(true); setMonthBtn(false); setYearBtn(false) }}>
                        <FormattedMessage id="project.tracking.week" />
                    </button>
                    <button className={monthBtn ? "btn primary graph" : "btn notSelected"} onClick={() => { setTimeRange(29); setWeekBtn(false); setMonthBtn(true); setYearBtn(false) }}>
                        <FormattedMessage id="project.tracking.month" />
                    </button>
                    <button className={yearBtn ? "btn primary graph" : "btn notSelected"} onClick={() => { setTimeRange(364); setWeekBtn(false); setMonthBtn(false); setYearBtn(true) }}>
                        <FormattedMessage id="project.tracking.year" />
                    </button>
                </div>
                <br />
                <br />
                <Chart
                    data={chartData}
                    axes={axes}
                    getSeriesStyle={getSeriesStyle}
                    getDatumStyle={getDatumStyle}
                    tooltip={tooltip}
                    className="Graph"
                />
            </div>
        </div>
    );
};

export default GraphPage;
