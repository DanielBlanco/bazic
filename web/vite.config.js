import {resolve} from 'path'
import {minifyHtml, injectHtml} from 'vite-plugin-html'
import copy from 'rollup-plugin-copy'

const scalaVersion = '3.0.0'

// https://vitejs.dev/config/
export default ({mode}) => {
    const scalaJsDir = `../frontend/target/scala-${scalaVersion}/bazic-${mode === 'production' ? 'opt' : 'fastopt'}`
    const script = '<script type="module" src="main.js"></script>'

    return {
        server: {
            fs: {
              strict: false,
              // Allow serving files from one level up to the project root
              allow: ['..']
            },
            proxy: {
                '/api': {
                    target: 'http://localhost:9999',
                    changeOrigin: true,
                    rewrite: (path) => path.replace(/^\/api/, '')
                },
            }
        },
        plugins: [
            ...(process.env.NODE_ENV === 'production' ? [minifyHtml(),] : []),
            copy({
              targets: [
                { src: `${scalaJsDir}/*.js`, dest: 'public' }
              ],
              verbose: true
            }),
            injectHtml({
                injectData: { script }
            })
        ],
        resolve: {
            alias: {
              'stylesheets': resolve(
                __dirname,
                '..',
                'frontend',
                'src',
                'main',
                'static',
                'stylesheets'
              )
            }
        }
    }
}
