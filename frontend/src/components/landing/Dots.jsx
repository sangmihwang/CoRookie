import React from 'react'

const Dot = ({ index, currentIndex, onClick }) => {
    const selected = index === currentIndex
    return (
        <div
            style={{
                width: 15,
                height: 15,
                border: '1px solid' + (selected ? 'gray' : ' rgba(0, 0, 0, 0)'),
                borderRadius: 9999,
                margin: '10px 0',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
            }}
            onClick={() => onClick(index)}>
            <div
                style={{
                    position: 'relative',
                    width: 11,
                    height: 11,
                    borderRadius: 9999,
                    backgroundColor: 'lightgray',
                    cursor: 'pointer',
                }}></div>
        </div>
    )
}

const Dots = ({ limit, currentIndex, onDotClick }) => {
    return (
        <div style={{ position: 'fixed', top: 0, left: 100, height: '100%' }}>
            <div
                style={{
                    position: 'fixed',
                    top: 65,
                    left: 100 + 8,
                    height: '100%',
                    width: 1,
                    backgroundColor: 'white',
                }}></div>
            <div
                style={{
                    position: 'fixed',
                    display: 'flex',
                    flexDirection: 'column',
                    height: '100%',
                    alignItems: 'center',
                    justifyContent: 'center',
                }}>
                {Array(limit)
                    .fill('')
                    .map((_, index) => (
                        <Dot key={index} index={index} currentIndex={currentIndex} onClick={onDotClick}></Dot>
                    ))}
            </div>
        </div>
    )
}

export default Dots
